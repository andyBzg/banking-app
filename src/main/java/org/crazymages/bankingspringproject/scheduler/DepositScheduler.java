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

/**
 * A scheduler component for executing deposit interest payments.
 * It performs the calculation and payment of interest on deposit accounts.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DepositScheduler {

    private final AccountDatabaseService accountDatabaseService;
    private final AgreementDatabaseService agreementDatabaseService;
    private final ClientDatabaseService clientDatabaseService;
    private final TransactionDatabaseService transactionDatabaseService;

    /**
     * Executes deposit interest payments based on a scheduled cron expression.
     */
    @Scheduled(cron = "${deposit.schedule}")
    public void executeDepositsInterestPayments() {
        List<Account> depositAccounts = getActiveDepositAccounts();
        List<Agreement> agreements = getAgreements(depositAccounts);
        Account bankAccount = getBankAccount();

        for (Account depositAccount : depositAccounts) {
            BigDecimal interestRate = findInterestRate(agreements, depositAccount.getUuid());
            if (interestRate != null) {
                performInterestPayment(bankAccount, depositAccount, interestRate);
            }
        }
    }

    private Account getBankAccount() {
        UUID bankUuid = getBankUuid();
        return accountDatabaseService.findAllByClientId(bankUuid)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("bank account not found"));
    }

    private UUID getBankUuid() {
        return clientDatabaseService.findClientsByStatus(ClientStatus.BANK)
                .stream()
                .map(Client::getUuid)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("bank uuid not found"));
    }

    private List<Agreement> getAgreements(List<Account> depositAccounts) {
        return depositAccounts
                .stream()
                .flatMap(account -> agreementDatabaseService.findAgreementsByClientUuid(account.getClientUuid())
                        .stream())
                .filter(agreement -> agreement.getStatus() == AgreementStatus.ACTIVE)
                .toList();
    }

    private List<Account> getActiveDepositAccounts() {
        return accountDatabaseService
                .findAccountsByProductTypeAndStatus(ProductType.DEPOSIT_ACCOUNT, ProductStatus.ACTIVE)
                .stream()
                .filter(account -> account.getStatus() == AccountStatus.ACTIVE)
                .toList();
    }

    /**
     * Calculates the new balance after applying the interest rate to the current balance.
     *
     * @param balance      The current balance.
     * @param interestRate The interest rate to apply.
     * @return The new balance after applying the interest rate.
     */
    public BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal interestRate) {
        BigDecimal deposit = balance.multiply(interestRate.divide(BigDecimal.valueOf(100)));
        return balance.add(deposit);
    }

    /**
     * Finds the interest rate for a specific account UUID from the list of agreements.
     *
     * @param agreements   The list of agreements to search in.
     * @param accountUuid  The UUID of the account to find the interest rate for.
     * @return The interest rate for the account, or null if not found.
     */
    public BigDecimal findInterestRate(List<Agreement> agreements, UUID accountUuid) {
        return agreements.stream()
                .filter(agreement -> agreement.getAccountUuid().equals(accountUuid))
                .findFirst()
                .map(Agreement::getInterestRate)
                .orElseThrow(() -> new DataNotFoundException("not found"));
    }

    /**
     * Performs the interest payment by creating a transaction from the bank account to the recipient account.
     *
     * @param bankAccount      The bank account from which the payment is made.
     * @param recipientAccount The recipient account to receive the payment.
     * @param interestRate     The interest rate to apply to the payment.
     */
    public void performInterestPayment(Account bankAccount, Account recipientAccount, BigDecimal interestRate) {
        Transaction transaction = getTransaction(bankAccount, recipientAccount, interestRate);
        transactionDatabaseService.transferFunds(transaction);
    }

    /**
     * Creates a transaction from the bank account to the recipient account for the interest payment.
     *
     * @param bankAccount      The bank account from which the payment is made.
     * @param recipientAccount The recipient account to receive the payment.
     * @param interestRate     The interest rate to apply to the payment.
     * @return The created transaction.
     */
    public Transaction getTransaction(Account bankAccount, Account recipientAccount, BigDecimal interestRate) {
        Transaction transaction = new Transaction();
        transaction.setDebitAccountUuid(bankAccount.getUuid());
        transaction.setCreditAccountUuid(recipientAccount.getUuid());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setCurrencyCode(recipientAccount.getCurrencyCode());
        transaction.setAmount(calculateNewBalance(recipientAccount.getBalance(), interestRate));
        transaction.setDescription("Deposit Interest Payment");
        return transaction;
    }
}
