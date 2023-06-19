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
    public void transferFunds(Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        Account sender = accountDatabaseService.findById(transaction.getDebitAccountUuid());
        Account recipient = accountDatabaseService.findById(transaction.getCreditAccountUuid());

        if (sender.getBalance() == null || sender.getStatus() == null ||
                recipient.getBalance() == null || recipient.getStatus() == null) {
            throw new IllegalArgumentException();
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(String.valueOf(sender.getUuid()));
        }
        if (!sender.getStatus().equals(AccountStatus.ACTIVE) || !recipient.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new TransactionNotAllowedException();
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));

        accountDatabaseService.update(sender.getUuid(), sender);
        accountDatabaseService.update(recipient.getUuid(), recipient);
        transactionRepository.save(transaction);
        log.info("transfer saved to db");
    }
}
