package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionDatabaseService {

    void create(Transaction transaction);

    List<Transaction> findAll();

    Transaction findById(UUID uuid);

    List<Transaction> findOutgoingTransactions(UUID uuid);

    List<Transaction> findIncomingTransactions(UUID uuid);

    void transferFunds(Transaction transaction);

}
