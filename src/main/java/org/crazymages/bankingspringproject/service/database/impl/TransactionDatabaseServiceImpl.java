package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.TransactionDto;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.exception.TransactionNotAllowedException;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.InsufficientFundsException;
import org.crazymages.bankingspringproject.repository.TransactionRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.crazymages.bankingspringproject.service.utils.converter.CurrencyConverter;
import org.crazymages.bankingspringproject.dto.mapper.transaction.TransactionDtoMapper;
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
    private final TransactionDtoMapper transactionDtoMapper;
    private final AccountDatabaseService accountDatabaseService;
    private final ClientDatabaseService clientDatabaseService;
    private final CurrencyConverter currencyConverter;


    @Override
    @Transactional
    public void create(TransactionDto transactionDto) {
        Transaction transaction = transactionDtoMapper.mapDtoToEntity(transactionDto);
        transactionRepository.save(transaction);
        log.info("transaction created");
    }

    @Override
    @Transactional
    public List<TransactionDto> findAll() {
        log.info("retrieving list of transactions");
        List<Transaction> transactions = transactionRepository.findAll();
        return getDtoList(transactions);
    }

    @Override
    @Transactional
    public TransactionDto findById(String transactionUuid) {
        if (transactionUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(transactionUuid);
        log.info("retrieving transaction by id {}", uuid);
        return transactionDtoMapper.mapEntityToDto(
                transactionRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public List<TransactionDto> findOutgoingTransactions(String senderUuid) {
        if (senderUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(senderUuid);
        log.info("retrieving list of transactions by sender id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByDebitAccountUuid(uuid);
        return getDtoList(transactions);
    }

    @Override
    @Transactional
    public List<TransactionDto> findIncomingTransactions(String recipientUuid) {
        if (recipientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(recipientUuid);
        log.info("retrieving list of transactions by recipient id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByCreditAccountUuid(uuid);
        return getDtoList(transactions);
    }

    @Override
    @Transactional
    public List<TransactionDto> findAllTransactionsByClientId(String clientUuid) {
        if (clientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        log.info("retrieving list of transactions by client id {} ", uuid);
        List<Transaction> transactions = transactionRepository.findAllTransactionsWhereClientIdIs(uuid);
        return getDtoList(transactions);
    }

    @Override
    @Transactional
    public void transferFunds(TransactionDto transactionDto) {
        Transaction transaction = transactionDtoMapper.mapDtoToEntity(transactionDto);
        BigDecimal amount = transaction.getAmount();
        Account senderAccount = accountDatabaseService.findById(transaction.getDebitAccountUuid());
        Account recipientAccount = accountDatabaseService.findById(transaction.getCreditAccountUuid());

        checkAmount(amount);
        checkBalanceNotNull(senderAccount, recipientAccount);
        checkAccountStatusNotNull(senderAccount, recipientAccount);
        checkSufficientFunds(amount, senderAccount);
        checkAccountsStatusActive(senderAccount, recipientAccount);
        checkClientsStatusActive(senderAccount, recipientAccount);

        CurrencyCode senderCurrency = senderAccount.getCurrencyCode();
        transaction.setCurrencyCode(senderCurrency);
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));

        performTransfer(transaction, amount, senderAccount, recipientAccount);
    }

    private void performTransfer(Transaction transaction, BigDecimal amount, Account sender, Account recipient) {
        CurrencyCode senderCurrency = sender.getCurrencyCode();
        CurrencyCode recipientCurrency = recipient.getCurrencyCode();

        if (recipientCurrency.equals(senderCurrency)) {
            recipient.setBalance(recipient.getBalance().add(amount));
        } else {
            recipient = currencyConverter.performCurrencyConversion(amount, recipient, sender);
        }

        accountDatabaseService.update(sender.getUuid(), sender);
        accountDatabaseService.update(recipient.getUuid(), recipient);
        transactionRepository.save(transaction);
        log.info("transfer saved to db");
    }

    private void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount is less or equals Zero");
        }
    }

    private void checkBalanceNotNull(Account senderAccount, Account recipientAccount) {
        if (senderAccount.getBalance() == null || recipientAccount.getBalance() == null) {
            throw new IllegalArgumentException("Balance is null");
        }
    }

    private void checkAccountStatusNotNull(Account senderAccount, Account recipientAccount) {
        if (senderAccount.getStatus() == null || recipientAccount.getStatus() == null) {
            throw new IllegalArgumentException("Status is null");
        }
    }

    private void checkSufficientFunds(BigDecimal amount, Account senderAccount) {
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Sender balance is too low");
        }
    }

    private void checkAccountsStatusActive(Account senderAccount, Account recipientAccount) {
        AccountStatus status = AccountStatus.ACTIVE;
        if (!senderAccount.getStatus().equals(status) || !recipientAccount.getStatus().equals(status)) {
            throw new TransactionNotAllowedException("Account is not active");
        }
    }

    private void checkClientsStatusActive(Account senderAccount, Account recipientAccount) {
        boolean isSenderActive = clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid());
        boolean isRecipientActive = clientDatabaseService.isClientStatusActive(recipientAccount.getClientUuid());
        if (!isSenderActive || !isRecipientActive) {
            throw new TransactionNotAllowedException();
        }
    }

    @Override
    @Transactional
    public List<TransactionDto> findTransactionsByClientIdBetweenDates(String uuid, String from, String to) {
        if (uuid == null) {
            throw new IllegalArgumentException();
        }
        UUID clientUuid = UUID.fromString(uuid);
        log.info("retrieving list of transactions for client {}, between {} and {}", clientUuid, from, to);
        LocalDate localDateStart = LocalDate.parse(from);
        Timestamp start = Timestamp.valueOf(localDateStart.atStartOfDay());

        LocalDate localDateEnd = LocalDate.parse(to);
        Timestamp end = Timestamp.valueOf(localDateEnd.atStartOfDay());

        List<Transaction> transactions = transactionRepository.findTransactionsByClientIdBetweenDates(clientUuid, start, end);
        return getDtoList(transactions);
    }

    @Override
    @Transactional
    public List<TransactionDto> findTransactionsBetweenDates(String from, String to) {
        log.info("retrieving list of transactions between {} and {}", from, to);

        LocalDate localDateStart = LocalDate.parse(from);
        Timestamp timestampStart = Timestamp.valueOf(localDateStart.atStartOfDay());

        LocalDate localDateEnd = LocalDate.parse(to);
        Timestamp timestampEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

        List<Transaction> transactions = transactionRepository.findTransactionsBetweenDates(timestampStart, timestampEnd);
        return getDtoList(transactions);
    }

    private List<TransactionDto> getDtoList(List<Transaction> transactions) {
        return Optional.ofNullable(transactions)
                .orElse(Collections.emptyList())
                .stream()
                .map(transactionDtoMapper::mapEntityToDto)
                .toList();
    }
}
