package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionDatabaseServiceImpl implements TransactionDatabaseService {

    private final TransactionRepository transactionRepository;
    private final AccountDatabaseService accountDatabaseService;
    private final ClientDatabaseService clientDatabaseService;

    @Override
    public void create(Transaction transaction) {
        transactionRepository.save(transaction);
        log.info("transaction created");
    }

    @Override
    public List<Transaction> findAll() {
        log.info("retrieving list of transactions");
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findById(UUID uuid) {
        log.info("retrieving transaction by id {}", uuid);
        Optional<Transaction> transactionOptional = transactionRepository.findById(uuid);
        return transactionOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public List<Transaction> findOutgoingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by sender id {}", uuid);
        return transactionRepository.findTransactionsByDebitAccountUuid(uuid);
    }

    @Override
    @Transactional
    public List<Transaction> findIncomingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by recipient id {}", uuid);
        return transactionRepository.findTransactionsByCreditAccountUuid(uuid);
    }

    @Override
    @Transactional
    public List<Transaction> findAllTransactionsByClientId(UUID uuid) {
        log.info("retrieving list of transactions by client id {} ", uuid);
        return transactionRepository.findAllTransactionsWhereClientIdIs(uuid);
    }

    @Override
    @Transactional
    public void transferFunds(Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        Account senderAccount = accountDatabaseService.findById(transaction.getDebitAccountUuid());
        Account recipientAccount = accountDatabaseService.findById(transaction.getCreditAccountUuid());
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
}
