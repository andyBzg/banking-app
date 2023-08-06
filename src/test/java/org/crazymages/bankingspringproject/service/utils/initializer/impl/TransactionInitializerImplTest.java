package org.crazymages.bankingspringproject.service.utils.initializer.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionInitializerImplTest {

    TransactionInitializerImpl transactionInitializer;
    Account sender;
    Account recipient;

    @BeforeEach
    void setUp() {
        transactionInitializer = new TransactionInitializerImpl();

        sender = new Account();
        sender.setUuid(UUID.randomUUID());
        sender.setCurrencyCode(CurrencyCode.USD);

        recipient = new Account();
        recipient.setUuid(UUID.randomUUID());
        recipient.setCurrencyCode(CurrencyCode.EUR);
    }

    @Test
    void initializeTransaction_createsTransactionWithCorrectProperties() {
        // when
        Transaction transaction = transactionInitializer.initializeTransaction(sender, recipient);

        // then
        assertEquals(sender.getUuid(), transaction.getDebitAccountUuid());
        assertEquals(recipient.getUuid(), transaction.getCreditAccountUuid());
        assertEquals(sender.getCurrencyCode(), transaction.getCurrencyCode());
    }

    @Test
    void initializeTransaction_argumentsWithNullProperties_returnsTransactionWithNullProperties() {
        // given
        sender.setUuid(null);
        sender.setCurrencyCode(null);
        recipient.setUuid(null);

        // when
        Transaction transaction = transactionInitializer.initializeTransaction(sender, recipient);

        // then
        assertNull(transaction.getDebitAccountUuid());
        assertNull(transaction.getCreditAccountUuid());
        assertNull(transaction.getCurrencyCode());
    }

    @Test
    void initializeTransaction_withNullSender_throwsIllegalArgumentException() {
        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionInitializer.initializeTransaction(null, recipient));
    }

    @Test
    void initializeTransaction_withNullRecipient_throwsIllegalArgumentException() {
        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionInitializer.initializeTransaction(sender, null));
    }

    @Test
    void initializeTransaction_returnsNewTransactionInstance() {
        // when
        Transaction transaction = transactionInitializer.initializeTransaction(sender, recipient);

        // then
        assertNotNull(transaction);
        assertNotSame(transaction, transactionInitializer.initializeTransaction(sender, recipient));
    }
}
