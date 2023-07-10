package org.crazymages.bankingspringproject.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.TransactionDTO;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.crazymages.bankingspringproject.service.utils.creator.TransactionCreator;
import org.crazymages.bankingspringproject.service.utils.mapper.TransactionDTOMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A component class that executes recurring transactions
 * between same customer current and savings accounts on a scheduled basis.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTransactionExecutor {

    private final ClientDatabaseService clientDatabaseService;
    private final TransactionDatabaseService transactionDatabaseService;
    private final AccountDatabaseService accountDatabaseService;
    private final AgreementDatabaseService agreementDatabaseService;
    private final TransactionCreator transactionCreator;
    private final TransactionDTOMapper transactionDTOMapper;

    /**
     * Executes recurring transactions based on a scheduled cron expression.
     */
    @Scheduled(cron = "0 0 12 15 * *")
    public void executeRecurringTransactions() {
        List<Client> clients = clientDatabaseService.findClientsWithCurrentAndSavingsAccounts();
        for (Client client : clients) {
            if (isRecurringTransactionAllowed(client)) {
                executeTransaction(client);
            }
        }
        log.info("executing recurring transactions");
    }

    /**
     * Checks if recurring transactions are allowed for the given client.
     *
     * @param client The client for whom to check the recurring transaction allowance
     * @return {@code true} if recurring transactions are allowed, {@code false} otherwise
     */
    public boolean isRecurringTransactionAllowed(Client client) {
        Agreement agreement = agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid());
        return agreement.getStatus().equals(AgreementStatus.ACTIVE);
    }

    /**
     * Executes a transaction for the given client.
     *
     * @param client The client for whom to execute the transaction
     */
    public void executeTransaction(Client client) {
        Account currentAccount = accountDatabaseService.findCurrentByClientId(client.getUuid());
        Account savingsAccount = accountDatabaseService.findSavingsByClientId(client.getUuid());
        Agreement agreement = agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid());

        Transaction transaction = transactionCreator.apply(currentAccount, savingsAccount);
        transaction.setAmount(agreement.getAmount());

        TransactionDTO transactionDTO = transactionDTOMapper.mapToTransactionDTO(transaction);
        transactionDatabaseService.transferFunds(transactionDTO);
    }
}
