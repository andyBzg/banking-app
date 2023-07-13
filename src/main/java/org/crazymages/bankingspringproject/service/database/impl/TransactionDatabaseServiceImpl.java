package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AccountDTO;
import org.crazymages.bankingspringproject.dto.TransactionDTO;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.exception.TransactionNotAllowedException;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.InsufficientFundsException;
import org.crazymages.bankingspringproject.repository.TransactionRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.TransactionDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final TransactionDTOMapper transactionDTOMapper;
    private final AccountDatabaseService accountDatabaseService;
    private final ClientDatabaseService clientDatabaseService;
    private final CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;


    @Override
    @Transactional
    public void create(TransactionDTO transactionDTO) {
        Transaction transaction = transactionDTOMapper.mapDtoToEntity(transactionDTO);
        transactionRepository.save(transaction);
        log.info("transaction created");
    }

    @Override
    @Transactional
    public List<TransactionDTO> findAll() {
        log.info("retrieving list of transactions");
        List<Transaction> transactions = transactionRepository.findAll();
        return transactionDTOMapper.getListOfDTOs(transactions);
    }

    @Override
    @Transactional
    public TransactionDTO findById(UUID uuid) {
        log.info("retrieving transaction by id {}", uuid);
        return transactionDTOMapper.mapEntityToDto(
                transactionRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public List<TransactionDTO> findOutgoingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by sender id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByDebitAccountUuid(uuid);
        return transactionDTOMapper.getListOfDTOs(transactions);
    }

    @Override
    @Transactional
    public List<TransactionDTO> findIncomingTransactions(UUID uuid) {
        log.info("retrieving list of transactions by recipient id {}", uuid);
        List<Transaction> transactions = transactionRepository.findTransactionsByCreditAccountUuid(uuid);
        return transactionDTOMapper.getListOfDTOs(transactions);
    }

    @Override
    @Transactional
    public List<TransactionDTO> findAllTransactionsByClientId(UUID uuid) {
        log.info("retrieving list of transactions by client id {} ", uuid);
        List<Transaction> transactions = transactionRepository.findAllTransactionsWhereClientIdIs(uuid);
        return transactionDTOMapper.getListOfDTOs(transactions);
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

        CurrencyCode senderCurrency = senderAccount.getCurrencyCode();
        CurrencyCode recipientCurrency = recipientAccount.getCurrencyCode();

        transaction.setCurrencyCode(senderCurrency);
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));

        if (recipientCurrency.equals(senderCurrency)) {
            recipientAccount.setBalance(recipientAccount.getBalance().add(amount));
        } else {
            performCurrencyConversion(amount, recipientAccount, senderAccount);
        }

        accountDatabaseService.update(senderAccount.getUuid(), senderAccount);
        accountDatabaseService.update(recipientAccount.getUuid(), recipientAccount);
        transactionRepository.save(transaction);
        log.info("transfer saved to db");
    }

    private void performCurrencyConversion(BigDecimal amount, AccountDTO recipientAccount, AccountDTO senderAccount) {
        // Курс валюты получателя по отношению к доллару (базовой валюте)
        String recipientCurrencyCode = recipientAccount.getCurrencyCode().name();
        CurrencyExchangeRate recipientToBaseCurrencyRate = currencyExchangeRateDatabaseService.findById(recipientCurrencyCode);
        BigDecimal recipientCurrencyRate = recipientToBaseCurrencyRate.getExchangeRate();
        log.info("Курс валюты получателя к доллару {}", recipientCurrencyRate);

        // Курс валюты отправителя по отношению к доллару (базовой валюте)
        String senderCurrencyCode = senderAccount.getCurrencyCode().name();
        BigDecimal senderCurrencyRate = currencyExchangeRateDatabaseService
                .findById(senderCurrencyCode)
                .getExchangeRate();
        log.info("Курс валюты отправителя к доллару {}", senderCurrencyRate);

        // Расчет суммы в базовой валюте (долларах)
        BigDecimal baseCurrencyAmount = amount.divide(senderCurrencyRate, 2, RoundingMode.HALF_UP);
        log.info("сумма в долларах: {}", baseCurrencyAmount);

        // Расчет суммы в валюте получателя
        BigDecimal recipientAmount = baseCurrencyAmount.multiply(recipientCurrencyRate);
        log.info("сумма в валюте получателя: {}", recipientAmount);

        // Обновление баланса счета получателя
        BigDecimal recipientBalance = recipientAccount.getBalance();
        recipientAccount.setBalance(recipientBalance.add(recipientAmount));
    }

    @Override
    @Transactional
    public List<TransactionDTO> findTransactionsByClientIdBetweenDates(UUID clientUuid, String from, String to) {
        log.info("retrieving list of transactions for client {}, between {} and {}", clientUuid, from, to);
        LocalDate localDateStart = LocalDate.parse(from);
        Timestamp start = Timestamp.valueOf(localDateStart.atStartOfDay());

        LocalDate localDateEnd = LocalDate.parse(to);
        Timestamp end = Timestamp.valueOf(localDateEnd.atStartOfDay());

        List<Transaction> transactions = transactionRepository.findTransactionsByClientIdBetweenDates(clientUuid, start, end);
        return transactionDTOMapper.getListOfDTOs(transactions);
    }

    @Override
    @Transactional
    public List<TransactionDTO> findTransactionsBetweenDates(String from, String to) {
        log.info("retrieving list of transactions between {} and {}", from, to);

        LocalDate localDateStart = LocalDate.parse(from);
        Timestamp timestampStart = Timestamp.valueOf(localDateStart.atStartOfDay());

        LocalDate localDateEnd = LocalDate.parse(to);
        Timestamp timestampEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

        List<Transaction> transactions = transactionRepository.findTransactionsBetweenDates(timestampStart, timestampEnd);
        return transactionDTOMapper.getListOfDTOs(transactions);
    }
}
