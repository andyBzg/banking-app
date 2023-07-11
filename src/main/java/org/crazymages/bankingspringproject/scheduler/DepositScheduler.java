package org.crazymages.bankingspringproject.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.*;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.database.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepositScheduler {

    private final AccountDatabaseService accountDatabaseService;
    private final AgreementDatabaseService agreementDatabaseService;
    private final ClientDatabaseService clientDatabaseService;
    private final TransactionDatabaseService transactionDatabaseService;

    @Scheduled(cron = "${deposit.schedule}")
    public void executeDepositsInterestPayments() {
        // Найти депозитные аккаунты
        List<Account> depositAccounts = accountDatabaseService.findAccountsByProductTypeAndStatus(
                ProductType.DEPOSIT_ACCOUNT, ProductStatus.ACTIVE);

        // Проверить, что клиент и аккаунт не заблокированы
        depositAccounts = depositAccounts
                .stream()
                .filter(account -> account.getStatus() == AccountStatus.ACTIVE)
                .toList();

        // Для каждого аккаунта айти активные договора которые содержат процентные ставки по депозиту
        List<Agreement> agreements = depositAccounts
                .stream()
                .flatMap(account -> agreementDatabaseService.findAgreementsByClientUuid(account.getClientUuid())
                        .stream())
                .filter(agreement -> agreement.getStatus() == AgreementStatus.ACTIVE)
                .toList();

        // Найти аккаунт банка
        UUID bankUuid = clientDatabaseService.findClientsByStatus(ClientStatus.BANK)
                .stream()
                .map(Client::getUuid)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("bank uuid not found"));
        Account bankAccount = accountDatabaseService.findAllByClientId(bankUuid)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("bank account not found"));

        // Выполнить начисление процентов на депозитные аккаунты
        for (Account depositAccount : depositAccounts) {
            BigDecimal interestRate = findInterestRate(agreements, depositAccount.getUuid());
            if (interestRate != null) {
                // Выполнить начисление процентов на аккаунт
                performInterestPayment(bankAccount, depositAccount, interestRate);
            }
        }
    }

    public BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal interestRate) {
        BigDecimal deposit = balance.multiply(interestRate.divide(BigDecimal.valueOf(100)));
        return balance.add(deposit);
    }

    public BigDecimal findInterestRate(List<Agreement> agreements, UUID accountUuid) {
        return agreements.stream()
                .filter(agreement -> agreement.getAccountUuid().equals(accountUuid))
                .findFirst()
                .map(Agreement::getInterestRate)
                .orElse(null);
    }

    public void performInterestPayment(Account bankAccount, Account recepientAccount, BigDecimal interestRate) {
        // Создать транзакцию от имени банка
        Transaction transaction = getTransaction(bankAccount, recepientAccount, interestRate);

        // Выполнить транзакцию
        transactionDatabaseService.transferFunds(transaction);
    }

    public Transaction getTransaction(Account bankAccount, Account recepientAccount, BigDecimal interestRate) {
        Transaction transaction = new Transaction();
        transaction.setDebitAccountUuid(bankAccount.getUuid());
        transaction.setCreditAccountUuid(recepientAccount.getUuid());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setCurrencyCode(recepientAccount.getCurrencyCode());
        transaction.setAmount(calculateNewBalance(recepientAccount.getBalance(), interestRate));
        transaction.setDescription("Deposit Interest Payment");
        return transaction;
    }
}
