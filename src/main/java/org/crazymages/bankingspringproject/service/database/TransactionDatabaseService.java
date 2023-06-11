package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionDatabaseService {

    void create(Transaction transaction);

    List<Transaction> findAll();

    Transaction findById(UUID uuid);

    void delete(UUID uuid);
}
