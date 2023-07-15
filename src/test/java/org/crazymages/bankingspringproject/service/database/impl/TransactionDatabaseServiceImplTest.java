package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.TransactionDto;
import org.crazymages.bankingspringproject.entity.Transaction;
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

import java.sql.Timestamp;
import java.time.LocalDate;
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

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transactionDto = new TransactionDto();
        uuid = UUID.randomUUID();
        transactions = List.of(new Transaction(), new Transaction());
        expected = List.of(new TransactionDto(), new TransactionDto());
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

    /*@Test
    void transferFunds() {
        // given
        // when
        // then
    }*/

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
