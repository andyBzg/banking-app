package org.crazymages.bankingspringproject.controller;

import org.crazymages.bankingspringproject.dto.client.ClientDto;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    ClientDatabaseService clientDatabaseService;

    @InjectMocks
    ClientController clientController;

    String uuid;

    @BeforeEach
    void setUp() {
        uuid = "7bcf30be-8c6e-4e10-a73b-706849fc94dc";
    }

    @Test
    void findClientsWhereBalanceMoreThan_success() {
        // given
        BigDecimal balance = BigDecimal.valueOf(1000);
        List<ClientDto> expected = List.of(ClientDto.builder().build(), ClientDto.builder().build());
        when(clientDatabaseService.findClientsWhereBalanceMoreThan(balance)).thenReturn(expected);

        // when
        ResponseEntity<List<ClientDto>> actual = clientController.findClientsWhereBalanceMoreThan(balance);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(clientDatabaseService).findClientsWhereBalanceMoreThan(balance);
    }

    @Test
    void findClientsWhereBalanceMoreThan_withEmptyList_returnsNoContentStatus() {
        // given
        BigDecimal balance = BigDecimal.valueOf(1000);
        List<ClientDto> expected = Collections.emptyList();
        when(clientDatabaseService.findClientsWhereBalanceMoreThan(balance)).thenReturn(expected);

        // when
        ResponseEntity<List<ClientDto>> actual = clientController.findClientsWhereBalanceMoreThan(balance);

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(clientDatabaseService).findClientsWhereBalanceMoreThan(balance);
    }

    @Test
    void findClientsWhereTransactionMoreThan_success() {
        // given
        Integer count = 10;
        List<ClientDto> expected = List.of(ClientDto.builder().build(), ClientDto.builder().build());
        when(clientDatabaseService.findClientsWhereTransactionMoreThan(count)).thenReturn(expected);

        // when
        ResponseEntity<List<ClientDto>> actual = clientController.findClientsWhereTransactionMoreThan(count);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(clientDatabaseService).findClientsWhereTransactionMoreThan(count);
    }

    @Test
    void findClientsWhereTransactionMoreThan_withEmptyList_returnsNoContentStatus() {
        // given
        Integer count = 10;
        List<ClientDto> expected = Collections.emptyList();
        when(clientDatabaseService.findClientsWhereTransactionMoreThan(count)).thenReturn(expected);

        // when
        ResponseEntity<List<ClientDto>> actual = clientController.findClientsWhereTransactionMoreThan(count);

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(clientDatabaseService).findClientsWhereTransactionMoreThan(count);
    }

    @Test
    void calculateTotalBalanceByClientUuid_success() {
        // given
        BigDecimal expected = BigDecimal.valueOf(5000);
        when(clientDatabaseService.calculateTotalBalanceByClientUuid(uuid)).thenReturn(expected);

        // when
        ResponseEntity<BigDecimal> actual = clientController.calculateTotalBalanceByClientUuid(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(clientDatabaseService).calculateTotalBalanceByClientUuid(uuid);
    }
}
