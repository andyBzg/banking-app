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
import org.crazymages.bankingspringproject.service.utils.validator.ListValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
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
    private final ListValidator<Transaction> listValidator;

    /**
     * Creates a new Transaction entity and saves it to the database.
     *
     * @param transaction The Transaction entity to create.
     */
    @Override
    public void create(Transaction transaction) {
        transactionRepository.save(transaction);
        log.info("transaction created");
    }

    /**
     * Retrieves a list of all Transaction entities from the database.
     *
     * @return A list of all Transaction entities.
     */
    @Override
    public List<Transaction> findAll() {
        log.info("retrieving list of transactions");
        List<Transaction> transactions = transactionRepository.findAll();
        return listValidator.validate(transactions);
    }

    /**
     * Retrieves a Transaction entity from the database by its UUID.
     *
     * @param uuid The UUID of the Transaction entity to retrieve.
     * @return The Transaction entity with the specified UUID.
     * @throws DataNotFoundException if no Transaction entity is found with the specified UUID.
     */
    @Override
    public Transaction findById(UUID uuid) {
        log.info("retrieving transaction by id {}", uuid);
        Optional<Transaction> transactionOptional = transactionRepository.findById(uuid);
        return transactionOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    /**
     * Retrieves a list of outgoing Transaction entities from the database by the sender's UUID.
     *
     * @param uuid The UUID of the sender's Account entity.
     * @return A list of outgoing Transaction entities for the sender.
     */
    @Override
    @Transactional
    public List<Transaction> findOutgoingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by sender id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByDebitAccountUuid(uuid);
        return listValidator.validate(transactions);
    }

    /**
     * Retrieves a list of incoming Transaction entities from the database by the recipient's UUID.
     *
     * @param uuid The UUID of the recipient's Account entity.
     * @return A list of incoming Transaction entities for the recipient.
     */
    @Override
    @Transactional
    public List<Transaction> findIncomingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by recipient id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByCreditAccountUuid(uuid);
        return listValidator.validate(transactions);
    }

    /**
     * Retrieves a list of all Transaction entities from the database for a given client UUID.
     *
     * @param uuid The UUID of the client.
     * @return A list of all Transaction entities for the client.
     */
    @Override
    @Transactional
    public List<Transaction> findAllTransactionsByClientId(UUID uuid) {
        log.info("retrieving list of transactions by client id {} ", uuid);
        List<Transaction> transactions = transactionRepository.findAllTransactionsWhereClientIdIs(uuid);
        return listValidator.validate(transactions);
    }

    /**
     * Transfers funds between two accounts based on the details provided in the Transaction entity.
     * Updates the balances of the sender and recipient accounts accordingly.
     *
     * @param transaction The Transaction entity representing the fund transfer details.
     * @throws InsufficientFundsException     if the sender account does not have sufficient funds for the transfer.
     * @throws TransactionNotAllowedException if the sender or recipient accounts are not active or the client status is not active.
     */
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

    /**
     * Retrieves a list of Transaction entities from the database for a specific client within a given date range.
     *
     * @param clientUuid The UUID of the client.
     * @param from       The start date of the date range (inclusive) in the format "yyyy-MM-dd".
     * @param to         The end date of the date range (inclusive) in the format "yyyy-MM-dd".
     * @return A list of Transaction entities for the specified client within the date range.
     */
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

    /**
     * Retrieves a list of Transaction entities from the database within a given date range.
     *
     * @param from The start date of the date range (inclusive) in the format "yyyy-MM-dd".
     * @param to   The end date of the date range (inclusive) in the format "yyyy-MM-dd".
     * @return A list of Transaction entities within the specified date range.
     */
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
}
