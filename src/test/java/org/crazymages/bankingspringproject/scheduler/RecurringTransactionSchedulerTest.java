package org.crazymages.bankingspringproject.scheduler;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.crazymages.bankingspringproject.service.utils.initializer.TransactionInitializer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class RecurringTransactionSchedulerTest {

    @Mock
    ClientDatabaseService clientDatabaseService;
    @Mock
    TransactionDatabaseService transactionDatabaseService;
    @Mock
    AccountDatabaseService accountDatabaseService;
    @Mock
    AgreementDatabaseService agreementDatabaseService;
    @Mock
    TransactionInitializer transactionInitializer;

    @InjectMocks
    RecurringTransactionScheduler scheduler;

    // TODO rewrite test
//    @Test
    void executeRecurringTransactions_activeAgreement_executeTransaction() {
        // given
        UUID clientUuid = UUID.randomUUID();

        Client client = new Client();
        client.setUuid(clientUuid);

        Agreement agreement = new Agreement();
        agreement.setStatus(AgreementStatus.ACTIVE);
        when(agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid()))
                .thenReturn(agreement);

        Account currentAccount = new Account();
        currentAccount.setUuid(UUID.randomUUID());
        currentAccount.setType(AccountType.CURRENT);
        currentAccount.setClientUuid(clientUuid);

        Account savingsAccount = new Account();
        savingsAccount.setUuid(UUID.randomUUID());
        savingsAccount.setType(AccountType.SAVINGS);
        savingsAccount.setUuid(clientUuid);

        when(accountDatabaseService.findCurrentByClientId(client.getUuid()))
                .thenReturn(currentAccount);
        when(accountDatabaseService.findSavingsByClientId(client.getUuid()))
                .thenReturn(savingsAccount);

        Transaction transaction = new Transaction();
        when(transactionInitializer.initializeTransaction(currentAccount, savingsAccount))
                .thenReturn(transaction);

        // when
        scheduler.executeRecurringTransactions();

        // then
        verify(transactionDatabaseService).transferFunds(transaction);
    }

    @Test
    void executeRecurringTransactions_noClients_noTransactionsExecuted_ok() {
        // given
        when(clientDatabaseService.findClientsWithCurrentAndSavingsAccounts())
                .thenReturn(Collections.emptyList());

        // when
        scheduler.executeRecurringTransactions();

        // then
        verify(transactionDatabaseService, never()).transferFunds(any());
    }

    @Test
    void executeRecurringTransactions_inactiveAgreement_noTransactionExecuted_ok() {
        // given
        Client client = new Client();
        client.setUuid(UUID.randomUUID());

        Agreement agreement = new Agreement();
        agreement.setStatus(AgreementStatus.SUSPENDED);
        when(agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid()))
                .thenReturn(agreement);

        // when
        scheduler.executeRecurringTransactions();

        // then
        verify(transactionDatabaseService, never()).transferFunds(any());
    }

    @Test
    void isRecurringTransactionAllowed_activeAgreement_ok() {
        // given
        Agreement agreement = new Agreement();
        agreement.setStatus(AgreementStatus.ACTIVE);

        Client client = new Client();
        client.setUuid(UUID.randomUUID());

        when(agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid())).thenReturn(agreement);

        // when
        boolean actual = scheduler.isRecurringTransactionAllowed(client);

        // then
        assertTrue(actual);
        verify(agreementDatabaseService).findSavingsAgreementByClientId(client.getUuid());
    }

    @Test
    void isRecurringTransactionAllowed_inactiveAgreement_ok() {
        // given
        Agreement agreement = new Agreement();
        agreement.setStatus(AgreementStatus.ANNULLED);

        Client client = new Client();
        client.setUuid(UUID.randomUUID());

        when(agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid())).thenReturn(agreement);

        // when
        boolean actual = scheduler.isRecurringTransactionAllowed(client);

        // then
        assertFalse(actual);
        verify(agreementDatabaseService).findSavingsAgreementByClientId(client.getUuid());
    }

    @Test
    void executeTransaction() {
        // given
        Client client = new Client();
        client.setUuid(UUID.randomUUID());
        Account currentAccount = new Account();
        Account savingsAccount = new Account();
        Agreement agreement = new Agreement();
        Transaction transaction = new Transaction();

        when(accountDatabaseService.findCurrentByClientId(client.getUuid())).thenReturn(currentAccount);
        when(accountDatabaseService.findSavingsByClientId(client.getUuid())).thenReturn(savingsAccount);
        when(agreementDatabaseService.findSavingsAgreementByClientId(client.getUuid())).thenReturn(agreement);
        when(transactionInitializer.initializeTransaction(currentAccount, savingsAccount)).thenReturn(transaction);

        // when
        scheduler.executeTransaction(client);

        // then
        verify(accountDatabaseService).findCurrentByClientId(client.getUuid());
        verify(accountDatabaseService).findSavingsByClientId(client.getUuid());
        verify(agreementDatabaseService).findSavingsAgreementByClientId(client.getUuid());
        verify(transactionInitializer).initializeTransaction(currentAccount, savingsAccount);
        verify(transactionDatabaseService).transferFunds(transaction);
    }

}
