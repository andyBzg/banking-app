package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AccountDto;
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
import org.crazymages.bankingspringproject.service.utils.mapper.impl.AccountDtoMapper;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.TransactionDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
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
    private final AccountDtoMapper accountDtoMapper;
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
        return transactionDtoMapper.getDtoList(transactions);
    }

    @Override
    @Transactional
    public TransactionDto findById(UUID uuid) {
        log.info("retrieving transaction by id {}", uuid);
        return transactionDtoMapper.mapEntityToDto(
                transactionRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public List<TransactionDto> findOutgoingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by sender id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByDebitAccountUuid(uuid);
        return transactionDtoMapper.getDtoList(transactions);
    }

    @Override
    @Transactional
    public List<TransactionDto> findIncomingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by recipient id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByCreditAccountUuid(uuid);
        return transactionDtoMapper.getDtoList(transactions);
    }

    @Override
    @Transactional
    public List<TransactionDto> findAllTransactionsByClientId(UUID uuid) {
        log.info("retrieving list of transactions by client id {} ", uuid);
        List<Transaction> transactions = transactionRepository.findAllTransactionsWhereClientIdIs(uuid);
        return transactionDtoMapper.getDtoList(transactions);
    }

    @Override
    @Transactional
    public void transferFunds(Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        AccountDto senderAccountDto = accountDatabaseService.findById(transaction.getDebitAccountUuid());
        AccountDto recipientAccountDto = accountDatabaseService.findById(transaction.getCreditAccountUuid());
        Account senderAccount = accountDtoMapper.mapDtoToEntity(senderAccountDto);
        Account recipientAccount = accountDtoMapper.mapDtoToEntity(recipientAccountDto);
        boolean isSenderActive = clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid());
        boolean isRecipientActive = clientDatabaseService.isClientStatusActive(recipientAccount.getClientUuid());

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
        if (!isSenderActive || !isRecipientActive) {
            throw new TransactionNotAllowedException();
        }

        CurrencyCode senderCurrency = senderAccount.getCurrencyCode();
        CurrencyCode recipientCurrency = recipientAccount.getCurrencyCode();

        transaction.setCurrencyCode(senderCurrency);
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));

        if (recipientCurrency.equals(senderCurrency)) {
            recipientAccount.setBalance(recipientAccount.getBalance().add(amount));
        } else {
            recipientAccount = currencyConverter.performCurrencyConversion(amount, recipientAccount, senderAccount);
        }

        senderAccountDto = accountDtoMapper.mapEntityToDto(senderAccount);
        recipientAccountDto = accountDtoMapper.mapEntityToDto(recipientAccount);
        accountDatabaseService.update(senderAccount.getUuid(), senderAccountDto);
        accountDatabaseService.update(recipientAccount.getUuid(), recipientAccountDto);
        transactionRepository.save(transaction);
        log.info("transfer saved to db");
    }

    @Override
    @Transactional
    public List<TransactionDto> findTransactionsByClientIdBetweenDates(UUID clientUuid, String from, String to) {
        log.info("retrieving list of transactions for client {}, between {} and {}", clientUuid, from, to);
        LocalDate localDateStart = LocalDate.parse(from);
        Timestamp start = Timestamp.valueOf(localDateStart.atStartOfDay());

        LocalDate localDateEnd = LocalDate.parse(to);
        Timestamp end = Timestamp.valueOf(localDateEnd.atStartOfDay());

        List<Transaction> transactions = transactionRepository.findTransactionsByClientIdBetweenDates(clientUuid, start, end);
        return transactionDtoMapper.getDtoList(transactions);
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
        return transactionDtoMapper.getDtoList(transactions);
    }
}
