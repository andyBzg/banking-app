package org.crazymages.bankingspringproject.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.TransactionDto;
import org.crazymages.bankingspringproject.dto.mapper.transaction.TransactionDtoMapper;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.crazymages.bankingspringproject.service.utils.initializer.TransactionInitializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final TransactionInitializer transactionInitializer;
    private final TransactionDtoMapper transactionDtoMapper;

    /**
     * Executes deposit interest payments based on a scheduled cron expression.
     */
    @Scheduled(cron = "${deposit.schedule}")
    public void executeDepositsInterestPayments() {
        log.info("Executing deposit interest payments");
        List<Account> depositAccounts = getActiveDepositAccounts();
        List<Agreement> agreements = getAgreements(depositAccounts);
        Account bankAccount = getBankAccount();
        BigDecimal updatedBalance;

        for (Account depositAccount : depositAccounts) {
            BigDecimal interestRate = findInterestRate(agreements, depositAccount.getUuid());
            if (interestRate != null) {
                updatedBalance = applyInterestRate(depositAccount.getBalance(), interestRate);
                performInterestPayment(bankAccount, depositAccount, updatedBalance);
            }
        }
    }

    /**
     * Retrieves the bank account from the database.
     *
     * @return The bank account.
     * @throws DataNotFoundException if the bank account is not found.
     */
    public Account getBankAccount() {
        UUID bankUuid = getBankUuid();
        return accountDatabaseService.findAllByClientId(bankUuid)
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("Bank account not found"));
    }

    /**
     * Retrieves the UUID of the bank client from the database.
     *
     * @return The UUID of the bank client.
     * @throws DataNotFoundException if the bank UUID is not found.
     */
    public UUID getBankUuid() {
        return clientDatabaseService.findClientsByStatus(ClientStatus.BANK)
                .stream()
                .map(Client::getUuid)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("Bank UUID not found"));
    }

    /**
     * Retrieves the active agreements associated with the deposit accounts.
     *
     * @param depositAccounts The list of deposit accounts.
     * @return The list of active agreements.
     */
    public List<Agreement> getAgreements(List<Account> depositAccounts) {
        return depositAccounts
                .stream()
                .flatMap(account -> agreementDatabaseService.findAgreementsByClientUuid(account.getClientUuid())
                        .stream())
                .filter(agreement -> agreement.getStatus() == AgreementStatus.ACTIVE)
                .toList();
    }

    /**
     * Retrieves the active deposit accounts from the database.
     *
     * @return The list of active deposit accounts.
     */
    public List<Account> getActiveDepositAccounts() {
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
    public BigDecimal applyInterestRate(BigDecimal balance, BigDecimal interestRate) {
        BigDecimal deposit =
                balance.multiply(interestRate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        return balance.add(deposit);
    }

    /**
     * Finds the interest rate for a specific account UUID from the list of agreements.
     *
     * @param agreements  The list of agreements to search in.
     * @param accountUuid The UUID of the account to find the interest rate for.
     * @return The interest rate for the account, or null if not found.
     * @throws DataNotFoundException if the interest rates is not found.
     */
    public BigDecimal findInterestRate(List<Agreement> agreements, UUID accountUuid) {
        return agreements.stream()
                .filter(agreement -> agreement.getAccountUuid().equals(accountUuid))
                .findFirst()
                .map(Agreement::getInterestRate)
                .orElseThrow(() -> new DataNotFoundException("Interest rate not found"));
    }

    /**
     * Performs the interest payment by creating a transaction from the bank account to the recipient account.
     *
     * @param bankAccount      The bank account from which the payment is made.
     * @param recipientAccount The recipient account to receive the payment.
     * @param updatedBalance   The interest rate to apply to the payment.
     */
    public void performInterestPayment(Account bankAccount, Account recipientAccount, BigDecimal updatedBalance) {
        Transaction transaction = transactionInitializer.initializeTransaction(bankAccount, recipientAccount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(updatedBalance);
        transaction.setDescription("Deposit Interest Payment");
        TransactionDto transactionDto = transactionDtoMapper.mapEntityToDto(transaction);
        transactionDatabaseService.transferFunds(transactionDto);
    }
}
