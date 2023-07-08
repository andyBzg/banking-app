package org.crazymages.bankingspringproject.service.utils.creator;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * A component class that implements the BiFunction interface to create a Transaction object
 * based on the provided accounts information.
 */
@Component
public class TransactionCreator implements BiFunction<Account, Account, Transaction> {

    @Override
    public Transaction apply(Account current, Account savings) {
        Transaction transaction = new Transaction();
        transaction.setDebitAccountUuid(current.getUuid());
        transaction.setCreditAccountUuid(savings.getUuid());
        transaction.setType(TransactionType.RECURRING_PAYMENT);
        transaction.setCurrencyCode(current.getCurrencyCode());
        transaction.setDescription("Recurring payment");
        return transaction;
    }
}
