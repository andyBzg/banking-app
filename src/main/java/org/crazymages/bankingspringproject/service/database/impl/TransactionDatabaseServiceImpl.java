package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AccountDTO;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.exception.TransactionNotAllowedException;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.InsufficientFundsException;
import org.crazymages.bankingspringproject.repository.TransactionRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A service implementation for managing Transaction entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionDatabaseServiceImpl implements TransactionDatabaseService {

    private final TransactionRepository transactionRepository;
    private final AccountDatabaseService accountDatabaseService;
    private final ClientDatabaseService clientDatabaseService;


    @Override
    @Transactional
    public void create(Transaction transaction) {
        transactionRepository.save(transaction);
        log.info("transaction created");
    }

    @Override
    @Transactional
    public List<Transaction> findAll() {
        log.info("retrieving list of transactions");
        List<Transaction> transactions = transactionRepository.findAll();
        return checkListForNull(transactions);
    }

    @Override
    @Transactional
    public Transaction findById(UUID uuid) {
        log.info("retrieving transaction by id {}", uuid);
        Optional<Transaction> transactionOptional = transactionRepository.findById(uuid);
        return transactionOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public List<Transaction> findOutgoingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by sender id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByDebitAccountUuid(uuid);
        return checkListForNull(transactions);
    }

    @Override
    @Transactional
    public List<Transaction> findIncomingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by recipient id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByCreditAccountUuid(uuid);
        return checkListForNull(transactions);
    }

    @Override
    @Transactional
    public List<Transaction> findAllTransactionsByClientId(UUID uuid) {
        log.info("retrieving list of transactions by client id {} ", uuid);
        List<Transaction> transactions = transactionRepository.findAllTransactionsWhereClientIdIs(uuid);
        return checkListForNull(transactions);
    }

    @Override
    @Transactional
    public void transferFunds(Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        AccountDTO senderAccount = accountDatabaseService.findById(transaction.getDebitAccountUuid());
        AccountDTO recipientAccount = accountDatabaseService.findById(transaction.getCreditAccountUuid());
        boolean senderStatus = clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid());
        boolean recipientStatus = clientDatabaseService.isClientStatusActive(recipientAccount.getClientUuid());

        if (senderAccount.getBalance() == null || senderAccount.getStatus() == null ||
                recipientAccount.getBalance() == null || recipientAccount.getStatus() == null) {
            throw new IllegalArgumentException();
        }
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(String.valueOf(senderAccount.getUuid()));
        }
        if (!senderAccount.getStatus().equals(AccountStatus.ACTIVE) ||
                !recipientAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new TransactionNotAllowedException();
        }
        if (senderStatus || recipientStatus) {
            throw new TransactionNotAllowedException();
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        accountDatabaseService.update(senderAccount.getUuid(), senderAccount);
        accountDatabaseService.update(recipientAccount.getUuid(), recipientAccount);
        transactionRepository.save(transaction);
        log.info("transfer saved to db");
    }

    @Override
    @Transactional
    public List<Transaction> findTransactionsByClientIdBetweenDates(UUID clientUuid, String from, String to) {
        log.info("retrieving list of transactions for client {}, between {} and {}", clientUuid, from, to);
        LocalDate localDateStart = LocalDate.parse(from);
        Timestamp start = Timestamp.valueOf(localDateStart.atStartOfDay());

        LocalDate localDateEnd = LocalDate.parse(to);
        Timestamp end = Timestamp.valueOf(localDateEnd.atStartOfDay());
        return transactionRepository.findTransactionsByClientIdBetweenDates(clientUuid, start, end);
    }

    @Override
    @Transactional
    public List<Transaction> findTransactionsBetweenDates(String from, String to) {
        log.info("retrieving list of transactions between {} and {}", from, to);

        LocalDate localDateStart = LocalDate.parse(from);
        Timestamp timestampStart = Timestamp.valueOf(localDateStart.atStartOfDay());

        LocalDate localDateEnd = LocalDate.parse(to);
        Timestamp timestampEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

        return transactionRepository.findTransactionsBetweenDates(timestampStart, timestampEnd);
    }

    private List<Transaction> checkListForNull(List<Transaction> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
