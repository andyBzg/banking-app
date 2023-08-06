package org.crazymages.bankingspringproject.service.utils.initializer.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.service.utils.initializer.TransactionInitializer;
import org.springframework.stereotype.Component;

/**
 * A component class responsible for initializing new Transaction entities based on the provided sender and recipient Accounts.
 */
@Component
public class TransactionInitializerImpl implements TransactionInitializer {
    @Override
    public Transaction initializeTransaction(Account sender, Account recipient) {
        if (sender == null || recipient == null) {
            throw new IllegalArgumentException("argument is null");
        }
        Transaction transaction = new Transaction();
        transaction.setDebitAccountUuid(sender.getUuid());
        transaction.setCreditAccountUuid(recipient.getUuid());
        transaction.setCurrencyCode(sender.getCurrencyCode());
        return transaction;
    }
}
