package org.crazymages.bankingspringproject.service.utils.creator;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.springframework.stereotype.Component;

/**
 * A component class responsible for initializing new Transaction entities based on the provided sender and recipient Accounts.
 */
@Component
public class TransactionInitializer {

    /**
     * Initializes a new Transaction entity based on the provided sender and recipient Accounts.
     *
     * @param sender    The Account representing the sender of the transaction.
     * @param recipient The Account representing the recipient of the transaction.
     * @return The initialized Transaction entity.
     * @throws IllegalArgumentException if the provided recipient is null.
     */
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
