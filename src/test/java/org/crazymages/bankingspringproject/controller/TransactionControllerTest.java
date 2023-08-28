package org.crazymages.bankingspringproject.controller;

import org.crazymages.bankingspringproject.dto.transaction.TransactionDto;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @Mock
    TransactionDatabaseService transactionDatabaseService;

    @InjectMocks
    TransactionController transactionController;

    String uuid;

    @BeforeEach
    void setUp() {
        uuid = "7bcf30be-8c6e-4e10-a73b-706849fc94dc";
    }

    @Test
    void createTransaction_success() {
        // given
        TransactionDto transactionDto = TransactionDto.builder().build();
        TransactionDto createdTransactionDto = TransactionDto.builder().build();

        // when
        ResponseEntity<TransactionDto> actual = transactionController.createTransaction(transactionDto);

        // then
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(createdTransactionDto, actual.getBody());
        verify(transactionDatabaseService).create(transactionDto);
    }

    @Test
    void findAllTransactions_success() {
        // given
        List<TransactionDto> expected = List.of(TransactionDto.builder().build(), TransactionDto.builder().build());
        when(transactionDatabaseService.findAll()).thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.findAllTransactions();

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(transactionDatabaseService).findAll();
    }

    @Test
    void findAllTransactions_withEmptyList_returnsNoContentStatus() {
        // given
        List<TransactionDto> expected = Collections.emptyList();
        when(transactionDatabaseService.findAll()).thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.findAllTransactions();

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(transactionDatabaseService).findAll();
    }

    @Test
    void findTransactionByUuid_success() {
        // given
        TransactionDto expected = TransactionDto.builder().build();
        when(transactionDatabaseService.findById(uuid)).thenReturn(expected);

        // when
        ResponseEntity<TransactionDto> actual = transactionController.findTransactionByUuid(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(transactionDatabaseService).findById(uuid);
    }

    @Test
    void findOutgoingTransactions_success() {
        // given
        List<TransactionDto> expected = List.of(TransactionDto.builder().build(), TransactionDto.builder().build());
        when(transactionDatabaseService.findOutgoingTransactions(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.findOutgoingTransactions(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(transactionDatabaseService).findOutgoingTransactions(uuid);
    }

    @Test
    void findOutgoingTransactions_withEmptyList_returnsNoContentStatus() {
        // given
        List<TransactionDto> expected = Collections.emptyList();
        when(transactionDatabaseService.findOutgoingTransactions(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.findOutgoingTransactions(uuid);

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(transactionDatabaseService).findOutgoingTransactions(uuid);
    }

    @Test
    void findIncomingTransactions_success() {
        // given
        List<TransactionDto> expected = List.of(TransactionDto.builder().build(), TransactionDto.builder().build());
        when(transactionDatabaseService.findIncomingTransactions(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.findIncomingTransactions(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(transactionDatabaseService).findIncomingTransactions(uuid);
    }

    @Test
    void findIncomingTransactions_withEmptyList_returnsNoContentStatus() {
        // given
        List<TransactionDto> expected = Collections.emptyList();
        when(transactionDatabaseService.findIncomingTransactions(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.findIncomingTransactions(uuid);

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(transactionDatabaseService).findIncomingTransactions(uuid);
    }

    @Test
    void transferFunds_success() {
        // given
        TransactionDto transaction = TransactionDto.builder().build();

        // when
        ResponseEntity<String> actual = transactionController.transferFunds(transaction);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        verify(transactionDatabaseService).transferFunds(transaction);
    }

    @Test
    void findAllTransactionsByClientId_success() {
        // given
        List<TransactionDto> expected = List.of(TransactionDto.builder().build(), TransactionDto.builder().build());
        when(transactionDatabaseService.findAllTransactionsByClientId(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.findAllTransactions(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(transactionDatabaseService).findAllTransactionsByClientId(uuid);
    }

    @Test
    void findAllTransactionsByClientId_withEmptyList_returnsNoContentStatus() {
        // given
        List<TransactionDto> expected = Collections.emptyList();
        when(transactionDatabaseService.findAllTransactionsByClientId(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.findAllTransactions(uuid);

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(transactionDatabaseService).findAllTransactionsByClientId(uuid);
    }

    @Test
    void getTransactionStatementByClientUuidAndDateRange_success() {
        // given
        List<TransactionDto> expected = List.of(TransactionDto.builder().build(), TransactionDto.builder().build());
        String startDate = "2023-01-01";
        String endDate = "2023-01-31";
        when(transactionDatabaseService.findTransactionsByClientIdBetweenDates(uuid, startDate, endDate))
                .thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.getTransactionStatement(uuid, startDate, endDate);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(transactionDatabaseService).findTransactionsByClientIdBetweenDates(uuid, startDate, endDate);
    }

    @Test
    void getTransactionStatementByDateRange_success() {
        // given
        List<TransactionDto> expected = List.of(TransactionDto.builder().build(), TransactionDto.builder().build());
        String startDate = "2023-01-01";
        String endDate = "2023-01-31";
        when(transactionDatabaseService.findTransactionsBetweenDates(startDate, endDate))
                .thenReturn(expected);

        // when
        ResponseEntity<List<TransactionDto>> actual = transactionController.getTransactionStatement(startDate, endDate);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(transactionDatabaseService).findTransactionsBetweenDates(startDate, endDate);
    }
}
