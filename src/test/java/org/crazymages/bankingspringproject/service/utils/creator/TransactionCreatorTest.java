package org.crazymages.bankingspringproject.service.utils.creator;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCreatorTest {

    TransactionCreator transactionCreator;
    Account sender;
    Account recipient;

    @BeforeEach
    void setUp() {
        transactionCreator = new TransactionCreator();

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
        Transaction transaction = transactionCreator.apply(sender, recipient);

        // then
        assertEquals(sender.getUuid(), transaction.getDebitAccountUuid());
        assertEquals(recipient.getUuid(), transaction.getCreditAccountUuid());
        assertEquals(sender.getCurrencyCode(), transaction.getCurrencyCode());
    }

    @Test
    void apply_withNullSender_throwsNullPointerException() {
        // when, then
        assertThrows(NullPointerException.class, () -> transactionCreator.apply(null, recipient));
    }

    @Test
    void apply_withNullRecipient_throwsNullPointerException() {
        // when, then
        assertThrows(NullPointerException.class, () -> transactionCreator.apply(sender, null));
    }

    @Test
    void apply_returnsNewTransactionInstance() {
        // when
        Transaction transaction = transactionCreator.apply(sender, recipient);

        // then
        assertNotNull(transaction);
        assertNotSame(transaction, transactionCreator.apply(sender, recipient));
    }
}
