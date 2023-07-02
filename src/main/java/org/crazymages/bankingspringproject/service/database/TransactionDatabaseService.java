package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Transaction;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TransactionDatabaseService {

    void create(Transaction transaction);

    List<Transaction> findAll();

    Transaction findById(UUID uuid);

    List<Transaction> findOutgoingTransactions(UUID uuid);

    List<Transaction> findIncomingTransactions(UUID uuid);

    List<Transaction> findAllTransactionsByClientId(UUID uuid);

    void transferFunds(Transaction transaction);

    List<Transaction> findTransactionsByClientIdBetweenDates(UUID clientUuid, String from, String to);

    List<Transaction> findTransactionsBetweenDates(String from, String to);
}
