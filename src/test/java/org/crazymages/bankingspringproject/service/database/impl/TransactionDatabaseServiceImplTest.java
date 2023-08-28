package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.account.AccountDto;
import org.crazymages.bankingspringproject.dto.transaction.TransactionDto;
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
import org.crazymages.bankingspringproject.dto.account.mapper.AccountDtoMapper;
import org.crazymages.bankingspringproject.dto.transaction.mapper.TransactionDtoMapper;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

    UUID uuid;
    String strUuid;
    Transaction transaction;
    Transaction transaction1;
    Transaction transaction2;
    TransactionDto transactionDto;
    TransactionDto transactionDto1;
    TransactionDto transactionDto2;
    List<Transaction> transactions;
    List<TransactionDto> expected;
    AccountDto senderAccountDto;
    AccountDto recipientAccountDto;
    Account sender;
    Account recipient;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("f0621a3c-6849-4ef5-8fc2-7bf7dd450d26");
        strUuid = "f0621a3c-6849-4ef5-8fc2-7bf7dd450d26";

        transaction = new Transaction();
        transaction.setDebitAccountUuid(UUID.fromString("ed3a5e5a-cd77-4052-91fc-b042f2aa4dbe"));
        transaction.setCreditAccountUuid(UUID.fromString("b0e642b4-d957-4cee-b4ca-13839ad16a20"));

        transaction1 = new Transaction();
        transaction2 = new Transaction();

        sender = new Account();
        sender.setClientUuid(UUID.fromString("1989d4da-0f91-46d3-96c6-2b4a72950c89"));
        sender.setBalance(BigDecimal.valueOf(200));
        sender.setStatus(AccountStatus.ACTIVE);
        sender.setCurrencyCode(CurrencyCode.EUR);

        recipient = new Account();
        recipient.setClientUuid(UUID.fromString("7e3dc741-7e9a-4b60-9f96-da9fc0924927"));
        recipient.setBalance(BigDecimal.ZERO);
        recipient.setStatus(AccountStatus.ACTIVE);
        recipient.setCurrencyCode(CurrencyCode.EUR);

        transactionDto = TransactionDto.builder().build();
        transactionDto1 = TransactionDto.builder().build();
        transactionDto2 = TransactionDto.builder().build();
        transactions = List.of(transaction1, transaction2);
        expected = List.of(transactionDto1, transactionDto2);
        senderAccountDto = AccountDto.builder().build();
        recipientAccountDto = AccountDto.builder().build();
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
        when(transactionDtoMapper.mapEntityToDto(transaction1)).thenReturn(transactionDto1);
        when(transactionDtoMapper.mapEntityToDto(transaction2)).thenReturn(transactionDto2);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findAll();

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findAll();
        verify(transactionDtoMapper, times(2)).mapEntityToDto(any(Transaction.class));
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
        TransactionDto expected = TransactionDto.builder().build();
        when(transactionRepository.findById(uuid)).thenReturn(Optional.ofNullable(transaction));
        when(transactionDtoMapper.mapEntityToDto(transaction)).thenReturn(expected);

        // when
        TransactionDto actual = transactionDatabaseService.findById(String.valueOf(uuid));

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
        assertThrows(DataNotFoundException.class, () -> transactionDatabaseService.findById(strUuid));
        verify(transactionRepository).findById(uuid);
        verifyNoInteractions(transactionDtoMapper);
    }

    @Test
    void findById_invalidUuid_throwsIllegalArgumentException() {
        // given
        String invalidUuid = "invalid_uuid";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findById(invalidUuid));
    }

    @Test
    void findById_nullUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findById(null));
    }

    @Test
    void findOutgoingTransactions_success() {
        // given
        when(transactionRepository.findTransactionsByDebitAccountUuid(uuid)).thenReturn(transactions);
        when(transactionDtoMapper.mapEntityToDto(transaction1)).thenReturn(transactionDto1);
        when(transactionDtoMapper.mapEntityToDto(transaction2)).thenReturn(transactionDto2);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findOutgoingTransactions(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findTransactionsByDebitAccountUuid(uuid);
        verify(transactionDtoMapper, times(2)).mapEntityToDto(any(Transaction.class));
    }

    @Test
    void findOutgoingTransactions_invalidUuid_throwsIllegalArgumentException() {
        // given
        String invalidUuid = "invalid_uuid";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findOutgoingTransactions(invalidUuid));
    }

    @Test
    void findOutgoingTransactions_nullUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findOutgoingTransactions(null));
    }

    @Test
    void findIncomingTransactions_success() {
        /// given
        when(transactionRepository.findTransactionsByCreditAccountUuid(uuid)).thenReturn(transactions);
        when(transactionDtoMapper.mapEntityToDto(transaction1)).thenReturn(transactionDto1);
        when(transactionDtoMapper.mapEntityToDto(transaction2)).thenReturn(transactionDto2);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findIncomingTransactions(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findTransactionsByCreditAccountUuid(uuid);
        verify(transactionDtoMapper, times(2)).mapEntityToDto(any(Transaction.class));
    }

    @Test
    void findIncomingTransactions_invalidUuid_throwsIllegalArgumentException() {
        // given
        String invalidUuid = "invalid_uuid";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findIncomingTransactions(invalidUuid));
    }

    @Test
    void findIncomingTransactions_nullUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findIncomingTransactions(null));
    }

    @Test
    void findAllTransactionsByClientId_success() {
        // given
        when(transactionRepository.findAllTransactionsWhereClientIdIs(uuid)).thenReturn(transactions);
        when(transactionDtoMapper.mapEntityToDto(transaction1)).thenReturn(transactionDto1);
        when(transactionDtoMapper.mapEntityToDto(transaction2)).thenReturn(transactionDto2);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findAllTransactionsByClientId(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findAllTransactionsWhereClientIdIs(uuid);
        verify(transactionDtoMapper, times(2)).mapEntityToDto(any(Transaction.class));
    }

    @Test
    void findAllTransactionsByClientId_invalidUuid_throwsIllegalArgumentException() {
        // given
        String invalidUuid = "invalid_uuid";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findAllTransactionsByClientId(invalidUuid));
    }

    @Test
    void findAllTransactionsByClientId_nullUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findAllTransactionsByClientId(null));
    }

    @Test
    void transferFunds_validData_sameCurrency_success() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);
        when(clientDatabaseService.isClientStatusBlocked(sender.getClientUuid())).thenReturn(false);
        when(clientDatabaseService.isClientStatusBlocked(recipient.getClientUuid())).thenReturn(false);

        // when
        transactionDatabaseService.transferFunds(transactionDto);

        // then
        verify(transactionDtoMapper).mapDtoToEntity(transactionDto);
        verify(accountDatabaseService).findById(transaction.getDebitAccountUuid());
        verify(accountDatabaseService).findById(transaction.getCreditAccountUuid());
        verify(clientDatabaseService).isClientStatusBlocked(sender.getClientUuid());
        verify(clientDatabaseService).isClientStatusBlocked(recipient.getClientUuid());
        verify(accountDatabaseService).update(sender.getUuid(), sender);
        verify(accountDatabaseService).update(recipient.getUuid(), recipient);
        verify(transactionRepository).save(transaction);
        verifyNoInteractions(currencyConverter);
        assertEquals(recipient.getBalance(), amount);
    }

    @Test
    void transferFunds_validData_differentCurrencies_success() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        sender.setCurrencyCode(CurrencyCode.GBP);
        recipient.setCurrencyCode(CurrencyCode.AUD);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);
        when(clientDatabaseService.isClientStatusBlocked(sender.getClientUuid())).thenReturn(false);
        when(clientDatabaseService.isClientStatusBlocked(recipient.getClientUuid())).thenReturn(false);
        when(currencyConverter.performCurrencyConversion(amount, recipient, sender)).thenReturn(recipient);

        // when
        transactionDatabaseService.transferFunds(transactionDto);

        // then
        verify(transactionDtoMapper).mapDtoToEntity(transactionDto);
        verify(accountDatabaseService).findById(transaction.getDebitAccountUuid());
        verify(accountDatabaseService).findById(transaction.getCreditAccountUuid());
        verify(clientDatabaseService).isClientStatusBlocked(sender.getClientUuid());
        verify(clientDatabaseService).isClientStatusBlocked(recipient.getClientUuid());
        verify(accountDatabaseService).update(sender.getUuid(), sender);
        verify(accountDatabaseService).update(recipient.getUuid(), recipient);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void transferFunds_withNegativeAmount_throwsIllegalArgumentException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(-100);
        transaction.setAmount(amount);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
        verify(transactionDtoMapper).mapDtoToEntity(transactionDto);
        verify(accountDatabaseService, times(2)).findById(any(UUID.class));
        verifyNoInteractions(clientDatabaseService);
        verifyNoInteractions(currencyConverter);
        verifyNoMoreInteractions(accountDatabaseService);
        verify(transactionRepository, never()).save(transaction);
    }

    @Test
    void transferFunds_nullSenderBalance_throwsIllegalArgumentException_throwsIllegalArgumentException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        sender.setBalance(null);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
    }

    @Test
    void transferFunds_nullRecipientBalance_throwsIllegalArgumentException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        recipient.setBalance(null);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
    }

    @Test
    void transferFunds_nullSenderAccountStatus_throwsIllegalArgumentException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        sender.setStatus(null);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
    }

    @Test
    void transferFunds_nullRecipientAccount_throwsIllegalArgumentException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        recipient.setStatus(null);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
    }

    @Test
    void transferFunds_senderBalanceIsTooLow_throwsInsufficientFundsException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        sender.setBalance(BigDecimal.valueOf(50));

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);

        // when, then
        assertThrows(InsufficientFundsException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
        verify(transactionDtoMapper).mapDtoToEntity(transactionDto);
        verifyNoInteractions(currencyConverter);
        verifyNoInteractions(clientDatabaseService);
        verifyNoMoreInteractions(accountDtoMapper);
        verifyNoMoreInteractions(accountDatabaseService);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void transferFunds_senderAccountIsNotActive_throwsTransactionNotAllowedException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        sender.setStatus(AccountStatus.CLOSED);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);

        // when, then
        assertThrows(TransactionNotAllowedException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
        verify(transactionDtoMapper).mapDtoToEntity(transactionDto);
        verifyNoInteractions(currencyConverter);
        verifyNoInteractions(clientDatabaseService);
        verifyNoMoreInteractions(accountDtoMapper);
        verifyNoMoreInteractions(accountDatabaseService);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void transferFunds_recipientIsNotActive_throwsTransactionNotAllowedException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);
        recipient.setStatus(AccountStatus.CLOSED);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);

        // when, then
        assertThrows(TransactionNotAllowedException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
        verify(transactionDtoMapper).mapDtoToEntity(transactionDto);
        verifyNoInteractions(currencyConverter);
        verifyNoInteractions(clientDatabaseService);
        verifyNoMoreInteractions(accountDtoMapper);
        verifyNoMoreInteractions(accountDatabaseService);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void transferFunds_clientNotActive_throwsTransactionNotAllowedException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        transaction.setAmount(amount);

        when(transactionDtoMapper.mapDtoToEntity(transactionDto)).thenReturn(transaction);
        when(accountDatabaseService.findById(transaction.getDebitAccountUuid())).thenReturn(sender);
        when(accountDatabaseService.findById(transaction.getCreditAccountUuid())).thenReturn(recipient);
        when(clientDatabaseService.isClientStatusBlocked(sender.getClientUuid())).thenReturn(false);
        when(clientDatabaseService.isClientStatusBlocked(recipient.getClientUuid())).thenReturn(true);

        // when, then
        assertThrows(TransactionNotAllowedException.class, () -> transactionDatabaseService.transferFunds(transactionDto));
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
        when(transactionDtoMapper.mapEntityToDto(transaction1)).thenReturn(transactionDto1);
        when(transactionDtoMapper.mapEntityToDto(transaction2)).thenReturn(transactionDto2);

        // when
        List<TransactionDto> actual = transactionDatabaseService
                .findTransactionsByClientIdBetweenDates(String.valueOf(uuid), "2023-07-15", "2023-07-16");

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findTransactionsByClientIdBetweenDates(uuid, start, end);
        verify(transactionDtoMapper, times(2)).mapEntityToDto(any(Transaction.class));
    }

    @Test
    void findTransactionsByClientIdBetweenDates_invalidUuid_throwsIllegalArgumentException() {
        // given
        String invalidUuid = "invalid_uuid";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findById(invalidUuid));
    }

    @Test
    void findTransactionsByClientIdBetweenDates_nullUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionDatabaseService.findById(null));
    }

    @Test
    void findTransactionsByClientIdBetweenDates_invalidDate_throwsDateTimeParseException() {
        // given
        String from = "2023-07-15";
        String to = "0000-00-00";
        String strUuid = String.valueOf(uuid);

        // when
        assertThrows(DateTimeParseException.class, () -> transactionDatabaseService
                .findTransactionsByClientIdBetweenDates(strUuid, from, to));

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
                .findTransactionsByClientIdBetweenDates(String.valueOf(uuid), "2023-07-15", "2023-07-16");

        // then
        assertTrue(actual.isEmpty());
        verify(transactionRepository).findTransactionsByClientIdBetweenDates(uuid, start, end);
        verify(transactionDtoMapper, never()).mapEntityToDto(any(Transaction.class));
    }

    @Test
    void findTransactionsBetweenDates_success() {
        // given
        String from = "2023-07-15";
        String to = "2023-07-16";
        Timestamp start = Timestamp.valueOf(LocalDate.parse(from).atStartOfDay());
        Timestamp end = Timestamp.valueOf(LocalDate.parse(to).atStartOfDay());
        when(transactionRepository.findTransactionsBetweenDates(start, end)).thenReturn(transactions);
        when(transactionDtoMapper.mapEntityToDto(transaction1)).thenReturn(transactionDto1);
        when(transactionDtoMapper.mapEntityToDto(transaction2)).thenReturn(transactionDto2);

        // when
        List<TransactionDto> actual = transactionDatabaseService.findTransactionsBetweenDates(from, to);

        // then
        assertEquals(expected, actual);
        verify(transactionRepository).findTransactionsBetweenDates(start, end);
        verify(transactionDtoMapper, times(2)).mapEntityToDto(any(Transaction.class));
    }
}
