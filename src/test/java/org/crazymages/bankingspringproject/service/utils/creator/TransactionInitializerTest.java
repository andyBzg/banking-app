package org.crazymages.bankingspringproject.service.utils.creator;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionInitializerTest {

    TransactionInitializer transactionInitializer;
    Account sender;
    Account recipient;

    @BeforeEach
    void setUp() {
        transactionInitializer = new TransactionInitializer();

        sender = new Account();
        sender.setUuid(UUID.randomUUID());
        sender.setCurrencyCode(CurrencyCode.USD);

        recipient = new Account();
        recipient.setUuid(UUID.randomUUID());
        recipient.setCurrencyCode(CurrencyCode.EUR);
    }

    @Test
    void apply_createsTransactionWithCorrectProperties() {
        // when
        Transaction transaction = transactionInitializer.initializeTransaction(sender, recipient);

        // then
        assertEquals(sender.getUuid(), transaction.getDebitAccountUuid());
        assertEquals(recipient.getUuid(), transaction.getCreditAccountUuid());
        assertEquals(sender.getCurrencyCode(), transaction.getCurrencyCode());
    }

    @Test
    void apply_withNullSender_throwsNullPointerException() {
        // when, then
        assertThrows(NullPointerException.class, () -> transactionInitializer.initializeTransaction(null, recipient));
    }

    @Test
    void apply_withNullRecipient_throwsNullPointerException() {
        // when, then
        assertThrows(NullPointerException.class, () -> transactionInitializer.initializeTransaction(sender, null));
    }

    @Test
    void apply_returnsNewTransactionInstance() {
        // when
        Transaction transaction = transactionInitializer.initializeTransaction(sender, recipient);

        // then
        assertNotNull(transaction);
        assertNotSame(transaction, transactionInitializer.initializeTransaction(sender, recipient));
    }
}
