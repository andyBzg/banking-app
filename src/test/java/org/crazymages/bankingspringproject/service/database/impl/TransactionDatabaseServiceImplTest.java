package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.dto.TransactionDto;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.InsufficientFundsException;
import org.crazymages.bankingspringproject.exception.TransactionNotAllowedException;
import org.crazymages.bankingspringproject.repository.TransactionRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.utils.converter.CurrencyConverter;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.AccountDtoMapper;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.TransactionDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionDatabaseServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;
    @Mock
    TransactionDtoMapper transactionDtoMapper;
    @Mock
    AccountDatabaseService accountDatabaseService;
    @Mock
    AccountDtoMapper accountDtoMapper;
    @Mock
    ClientDatabaseService clientDatabaseService;
    @Mock
    CurrencyConverter currencyConverter;

    @InjectMocks
    TransactionDatabaseServiceImpl transactionDatabaseService;

    Transaction transaction;
    TransactionDto transactionDto;
    UUID uuid;
    List<Transaction> transactions;
    List<TransactionDto> expected;
    AccountDto senderAccountDto;
    AccountDto recipientAccountDto;
    Account senderAccount;
    Account recipientAccount;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transactionDto = new TransactionDto();
        uuid = UUID.randomUUID();
        transactions = List.of(new Transaction(), new Transaction());
        expected = List.of(new TransactionDto(), new TransactionDto());
        senderAccountDto = new AccountDto();
        recipientAccountDto = new AccountDto();
        senderAccount = new Account();
        recipientAccount = new Account();
    }

    @Test
    void create_success() {
        // given
        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);

        // when
        transactionDatabaseService.create(transactionDto);

        // then
        verify(transactionDtoMapper).mapDtoToEntity(transactionDto);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void findAll_success() {
        // given
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transactionDtoMapper.getDtoList(transactions)).thenReturn(expected);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findAll();

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findAll();
        verify(transactionDtoMapper).getDtoList(transactions);
    }

    @Test
    void findAll_withNull_emptyListReturned() {
        // given
        when(transactionRepository.findAll()).thenReturn(null);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findAll();

        //then
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_success() {
        // given
        TransactionDto expected = new TransactionDto();
        when(transactionRepository.findById(uuid)).thenReturn(Optional.ofNullable(transaction));
        when(transactionDtoMapper.mapEntityToDto(transaction)).thenReturn(expected);

        // when
        TransactionDto actual = transactionDatabaseService.findById(uuid);

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findById(uuid);
        verify(transactionDtoMapper).mapEntityToDto(transaction);
    }

    @Test
    void findById_nonExistentTransaction_throwsDataNotFoundException() {
        // given
        when(transactionRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> transactionDatabaseService.findById(uuid));
        verify(transactionRepository).findById(uuid);
        verifyNoInteractions(transactionDtoMapper);
    }

    @Test
    void findOutgoingTransactions_success() {
        // given
        when(transactionRepository.findTransactionsByDebitAccountUuid(uuid)).thenReturn(transactions);
        when(transactionDtoMapper.getDtoList(transactions)).thenReturn(expected);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findOutgoingTransactions(uuid);

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findTransactionsByDebitAccountUuid(uuid);
        verify(transactionDtoMapper).getDtoList(transactions);
    }

    @Test
    void findIncomingTransactions_success() {
        /// given
        when(transactionRepository.findTransactionsByCreditAccountUuid(uuid)).thenReturn(transactions);
        when(transactionDtoMapper.getDtoList(transactions)).thenReturn(expected);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findIncomingTransactions(uuid);

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findTransactionsByCreditAccountUuid(uuid);
        verify(transactionDtoMapper).getDtoList(transactions);
    }

    @Test
    void findAllTransactionsByClientId_success() {
        // given
        when(transactionRepository.findAllTransactionsWhereClientIdIs(uuid)).thenReturn(transactions);
        when(transactionDtoMapper.getDtoList(transactions)).thenReturn(expected);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findAllTransactionsByClientId(uuid);

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findAllTransactionsWhereClientIdIs(uuid);
        verify(transactionDtoMapper).getDtoList(transactions);
    }

    @Test
    void transferFunds_validData_sameCurrency_success() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        transaction.setDebitAccountUuid(UUID.randomUUID());
        transaction.setCreditAccountUuid(UUID.randomUUID());
        UUID senderUuid = UUID.randomUUID();
        UUID recipientUuid = UUID.randomUUID();

        senderAccountDto.setUuid(String.valueOf(senderUuid));
        recipientAccountDto.setUuid(String.valueOf(recipientUuid));

        senderAccount.setUuid(senderUuid);
        senderAccount.setBalance(BigDecimal.valueOf(200));
        senderAccount.setCurrencyCode(CurrencyCode.EUR);
        senderAccount.setStatus(AccountStatus.ACTIVE);

        recipientAccount.setUuid(recipientUuid);
        recipientAccount.setBalance(BigDecimal.valueOf(0));
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);
        recipientAccount.setStatus(AccountStatus.ACTIVE);

        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(senderAccountDto);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipientAccountDto);
        when(accountDtoMapper.mapDtoToEntity(senderAccountDto)).thenReturn(senderAccount);
        when(accountDtoMapper.mapDtoToEntity(recipientAccountDto)).thenReturn(recipientAccount);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);
        when(accountDtoMapper.mapEntityToDto(senderAccount)).thenReturn(senderAccountDto);
        when(accountDtoMapper.mapEntityToDto(recipientAccount)).thenReturn(recipientAccountDto);


        // when
        transactionDatabaseService.transferFunds(transaction);


        // then
        verify(accountDatabaseService, times( 2)).findById(any(UUID.class));
        verify(accountDtoMapper, times(2)).mapDtoToEntity(any(AccountDto.class));
        verify(clientDatabaseService, times(2)).isClientStatusActive(any());
        verify(accountDtoMapper, times(2)).mapEntityToDto(any(Account.class));
        verifyNoInteractions(currencyConverter);
        verify(accountDatabaseService, times(2)).update(any(UUID.class), any(AccountDto.class));
        verify(accountDatabaseService, times(2)).update(any(UUID.class), any(AccountDto.class));
        verify(transactionRepository).save(transaction);
        assertEquals(recipientAccount.getBalance(), amount);
    }

    @Test
    void transferFunds_validData_differentCurrencies_success() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        transaction.setDebitAccountUuid(UUID.randomUUID());
        transaction.setCreditAccountUuid(UUID.randomUUID());
        UUID senderUuid = UUID.randomUUID();
        UUID recipientUuid = UUID.randomUUID();

        senderAccountDto.setUuid(String.valueOf(senderUuid));
        recipientAccountDto.setUuid(String.valueOf(recipientUuid));

        senderAccount.setUuid(senderUuid);
        senderAccount.setBalance(BigDecimal.valueOf(200));
        senderAccount.setCurrencyCode(CurrencyCode.GBP);
        senderAccount.setStatus(AccountStatus.ACTIVE);

        recipientAccount.setUuid(recipientUuid);
        recipientAccount.setBalance(BigDecimal.valueOf(0));
        recipientAccount.setCurrencyCode(CurrencyCode.AUD);
        recipientAccount.setStatus(AccountStatus.ACTIVE);

        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(senderAccountDto);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipientAccountDto);
        when(accountDtoMapper.mapDtoToEntity(senderAccountDto)).thenReturn(senderAccount);
        when(accountDtoMapper.mapDtoToEntity(recipientAccountDto)).thenReturn(recipientAccount);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);
        when(currencyConverter.performCurrencyConversion(amount, recipientAccount, senderAccount))
                .thenReturn(recipientAccount);
        when(accountDtoMapper.mapEntityToDto(senderAccount)).thenReturn(senderAccountDto);
        when(accountDtoMapper.mapEntityToDto(recipientAccount)).thenReturn(recipientAccountDto);


        // when
        transactionDatabaseService.transferFunds(transaction);


        // then
        verify(accountDatabaseService, times( 2)).findById(any(UUID.class));
        verify(accountDtoMapper, times(2)).mapDtoToEntity(any(AccountDto.class));
        verify(clientDatabaseService, times(2)).isClientStatusActive(any());
        verify(accountDtoMapper, times(2)).mapEntityToDto(any(Account.class));
        verify(currencyConverter).performCurrencyConversion(amount, recipientAccount, senderAccount);
        verify(accountDatabaseService, times(2)).update(any(UUID.class), any(AccountDto.class));
        verify(accountDatabaseService, times(2)).update(any(UUID.class), any(AccountDto.class));
        verify(transactionRepository).save(transaction);
    }

    @Test
    void transferFunds_atLeastOneOfEntityFieldIsNull_throwsIllegalArgumentException() {
        // given
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(senderAccountDto);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipientAccountDto);
        when(accountDtoMapper.mapDtoToEntity(senderAccountDto)).thenReturn(senderAccount);
        when(accountDtoMapper.mapDtoToEntity(recipientAccountDto)).thenReturn(recipientAccount);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.transferFunds(transaction));
        verifyNoInteractions(currencyConverter);
        verifyNoMoreInteractions(accountDtoMapper);
        verifyNoMoreInteractions(accountDatabaseService);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void transferFunds_senderBalanceIsTooLow_throwsInsufficientFundsException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        transaction.setDebitAccountUuid(UUID.randomUUID());
        transaction.setCreditAccountUuid(UUID.randomUUID());
        UUID senderUuid = UUID.randomUUID();
        UUID recipientUuid = UUID.randomUUID();

        senderAccountDto.setUuid(String.valueOf(senderUuid));
        recipientAccountDto.setUuid(String.valueOf(recipientUuid));

        senderAccount.setUuid(senderUuid);
        senderAccount.setBalance(BigDecimal.valueOf(0));
        senderAccount.setCurrencyCode(CurrencyCode.EUR);
        senderAccount.setStatus(AccountStatus.ACTIVE);

        recipientAccount.setUuid(recipientUuid);
        recipientAccount.setBalance(BigDecimal.valueOf(0));
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);
        recipientAccount.setStatus(AccountStatus.ACTIVE);

        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(senderAccountDto);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipientAccountDto);
        when(accountDtoMapper.mapDtoToEntity(senderAccountDto)).thenReturn(senderAccount);
        when(accountDtoMapper.mapDtoToEntity(recipientAccountDto)).thenReturn(recipientAccount);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);


        // when, then
        assertThrows(InsufficientFundsException.class, () -> transactionDatabaseService.transferFunds(transaction));
        verifyNoInteractions(currencyConverter);
        verifyNoMoreInteractions(accountDtoMapper);
        verifyNoMoreInteractions(accountDatabaseService);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void transferFunds_oneOfAccountsIsNotActive_throwsTransactionNotAllowedException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        transaction.setDebitAccountUuid(UUID.randomUUID());
        transaction.setCreditAccountUuid(UUID.randomUUID());
        UUID senderUuid = UUID.randomUUID();
        UUID recipientUuid = UUID.randomUUID();

        senderAccountDto.setUuid(String.valueOf(senderUuid));
        recipientAccountDto.setUuid(String.valueOf(recipientUuid));

        senderAccount.setUuid(senderUuid);
        senderAccount.setBalance(BigDecimal.valueOf(200));
        senderAccount.setCurrencyCode(CurrencyCode.EUR);
        senderAccount.setStatus(AccountStatus.ACTIVE);

        recipientAccount.setUuid(recipientUuid);
        recipientAccount.setBalance(BigDecimal.valueOf(0));
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);
        recipientAccount.setStatus(AccountStatus.OVERDUE);

        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(senderAccountDto);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipientAccountDto);
        when(accountDtoMapper.mapDtoToEntity(senderAccountDto)).thenReturn(senderAccount);
        when(accountDtoMapper.mapDtoToEntity(recipientAccountDto)).thenReturn(recipientAccount);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);
        when(clientDatabaseService.isClientStatusActive(recipientAccount.getClientUuid())).thenReturn(false);


        // when, then
        assertThrows(TransactionNotAllowedException.class, () -> transactionDatabaseService.transferFunds(transaction));
        verifyNoInteractions(currencyConverter);
        verifyNoMoreInteractions(accountDtoMapper);
        verifyNoMoreInteractions(accountDatabaseService);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void transferFunds_recipientIsNotActive_throwsTransactionNotAllowedException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        transaction.setDebitAccountUuid(UUID.randomUUID());
        transaction.setCreditAccountUuid(UUID.randomUUID());
        UUID senderUuid = UUID.randomUUID();
        UUID recipientUuid = UUID.randomUUID();

        senderAccountDto.setUuid(String.valueOf(senderUuid));
        recipientAccountDto.setUuid(String.valueOf(recipientUuid));

        senderAccount.setUuid(senderUuid);
        senderAccount.setBalance(BigDecimal.valueOf(200));
        senderAccount.setCurrencyCode(CurrencyCode.EUR);
        senderAccount.setStatus(AccountStatus.ACTIVE);

        recipientAccount.setUuid(recipientUuid);
        recipientAccount.setBalance(BigDecimal.valueOf(0));
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);
        recipientAccount.setStatus(AccountStatus.ACTIVE);

        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(senderAccountDto);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipientAccountDto);
        when(accountDtoMapper.mapDtoToEntity(senderAccountDto)).thenReturn(senderAccount);
        when(accountDtoMapper.mapDtoToEntity(recipientAccountDto)).thenReturn(recipientAccount);
        when(clientDatabaseService.isClientStatusActive(senderAccount.getClientUuid())).thenReturn(true);
        when(clientDatabaseService.isClientStatusActive(recipientAccount.getClientUuid())).thenReturn(false);


        // when, then
        assertThrows(TransactionNotAllowedException.class, () -> transactionDatabaseService.transferFunds(transaction));
        verifyNoInteractions(currencyConverter);
        verifyNoMoreInteractions(accountDtoMapper);
        verifyNoMoreInteractions(accountDatabaseService);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void findTransactionsByClientIdBetweenDates_success() {
        // given
        String from = "2023-07-15";
        String to = "2023-07-16";
        Timestamp start = Timestamp.valueOf(LocalDate.parse(from).atStartOfDay());
        Timestamp end = Timestamp.valueOf(LocalDate.parse(to).atStartOfDay());
        when(transactionRepository
                .findTransactionsByClientIdBetweenDates(uuid, start, end))
                .thenReturn(transactions);
        when(transactionDtoMapper.getDtoList(transactions)).thenReturn(expected);

        // when
        List<TransactionDto> actual = transactionDatabaseService
                .findTransactionsByClientIdBetweenDates(uuid, "2023-07-15", "2023-07-16");

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findTransactionsByClientIdBetweenDates(uuid, start, end);
        verify(transactionDtoMapper).getDtoList(transactions);
    }

    @Test
    void findTransactionsByClientIdBetweenDates_invalidDate_throwsDateTimeParseException() {
        // given
        String from = "2023-07-15";
        String to = "0000-00-00";

        // when
        assertThrows(DateTimeParseException.class, () -> transactionDatabaseService
                .findTransactionsByClientIdBetweenDates(uuid, from, to));

        // then
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionDtoMapper);
    }

    @Test
    void findTransactionsByClientIdBetweenDates_noTransactions_returnsEmptyList() {
        // given
        String from = "2023-07-15";
        String to = "2023-07-16";
        Timestamp start = Timestamp.valueOf(LocalDate.parse(from).atStartOfDay());
        Timestamp end = Timestamp.valueOf(LocalDate.parse(to).atStartOfDay());
        when(transactionRepository
                .findTransactionsByClientIdBetweenDates(uuid, start, end))
                .thenReturn(Collections.emptyList());

        // when
        List<TransactionDto> actual = transactionDatabaseService
                .findTransactionsByClientIdBetweenDates(uuid, "2023-07-15", "2023-07-16");

        // then
        assertTrue(actual.isEmpty());
        verify(transactionRepository).findTransactionsByClientIdBetweenDates(uuid, start, end);
        verify(transactionDtoMapper).getDtoList(anyList());
    }

    @Test
    void findTransactionsBetweenDates_success() {
        // given
        String from = "2023-07-15";
        String to = "2023-07-16";
        Timestamp start = Timestamp.valueOf(LocalDate.parse(from).atStartOfDay());
        Timestamp end = Timestamp.valueOf(LocalDate.parse(to).atStartOfDay());
        when(transactionRepository.findTransactionsBetweenDates(start, end)).thenReturn(transactions);
        when(transactionDtoMapper.getDtoList(transactions)).thenReturn(expected);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findTransactionsBetweenDates(from, to);

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findTransactionsBetweenDates(start, end);
        verify(transactionDtoMapper).getDtoList(transactions);
    }
}
