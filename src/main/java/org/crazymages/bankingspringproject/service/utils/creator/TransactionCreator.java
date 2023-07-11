package org.crazymages.bankingspringproject.service.utils.creator;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * A component class that implements the BiFunction interface to create a Transaction object
 * based on the provided accounts information.
 */
@Component
public class TransactionCreator implements BiFunction<Account, Account, Transaction> {

    @Override
    public Transaction apply(Account sender, Account recipient) {
        Transaction transaction = new Transaction();
        transaction.setDebitAccountUuid(sender.getUuid());
        transaction.setCreditAccountUuid(recipient.getUuid());
        transaction.setCurrencyCode(sender.getCurrencyCode());

        return transaction;
    }
}
