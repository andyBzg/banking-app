package org.crazymages.bankingspringproject.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTransactionExecutor {

    private final ClientDatabaseService clientDatabaseService;
    private final TransactionDatabaseService transactionDatabaseService;
    private final AccountDatabaseService accountDatabaseService;
    private final AgreementDatabaseService agreementDatabaseService;

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

    private boolean isRecurringTransactionAllowed(Client client) {
        Agreement agreement = agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid());
        return agreement.getStatus().equals(AgreementStatus.ACTIVE);
    }

    private void executeTransaction(Client client) {
        Account currentAccount = accountDatabaseService.findCurrentByClientId(client.getUuid());
        Account savingsAccount = accountDatabaseService.findSavingsByClientId(client.getUuid());
        Agreement agreement = agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid());
        Transaction transaction = createTransaction(currentAccount, savingsAccount);
        transaction.setAmount(agreement.getAmount());
        transactionDatabaseService.transferFunds(transaction);
    }

    public Transaction createTransaction(Account current, Account savings) {
        Transaction transaction = new Transaction();
        transaction.setDebitAccountUuid(current.getUuid());
        transaction.setCreditAccountUuid(savings.getUuid());
        transaction.setType(TransactionType.RECURRING_PAYMENT);
        transaction.setCurrencyCode(current.getCurrencyCode());
        transaction.setDescription("Recurring payment");
        return transaction;
    }
}
