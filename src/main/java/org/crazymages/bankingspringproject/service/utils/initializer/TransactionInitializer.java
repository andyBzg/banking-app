package org.crazymages.bankingspringproject.service.utils.initializer;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;

public interface TransactionInitializer {

    /**
     * Initializes a new Transaction entity based on the provided sender and recipient Accounts.
     *
     * @param sender    The Account representing the sender of the transaction.
     * @param recipient The Account representing the recipient of the transaction.
     * @return The initialized Transaction entity.
     */
    Transaction initializeTransaction(Account sender, Account recipient);
}
