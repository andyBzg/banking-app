package org.crazymages.bankingspringproject.controller;

import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    AccountDatabaseService accountDatabaseService;

    @InjectMocks
    AccountController accountController;

    UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("7bcf30be-8c6e-4e10-a73b-706849fc94dc");
    }

    @Test
    void createAccount_success() {
        // when
        AccountDto accountDto = new AccountDto();
        AccountDto createdAccountDto = new AccountDto();

        // when
        ResponseEntity<AccountDto> actual = accountController.createAccount(accountDto);

        // then
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(createdAccountDto, actual.getBody());
        verify(accountDatabaseService).create(accountDto);
    }

    @Test
    void createAccount_withClientUuid_success() {
        // when
        AccountDto accountDto = new AccountDto();
        AccountDto createdAccountDto = new AccountDto();

        // when
        ResponseEntity<AccountDto> actual = accountController.createAccount(accountDto, String.valueOf(uuid));

        // then
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(createdAccountDto, actual.getBody());
        verify(accountDatabaseService).create(accountDto, uuid);
    }

    @Test
    void findAllAccounts_success() {
        // given
        List<AccountDto> expected = List.of(new AccountDto(), new AccountDto());
        when(accountDatabaseService.findAllNotDeleted()).thenReturn(expected);

        // when
        ResponseEntity<List<AccountDto>> actual = accountController.findAllAccounts();

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).findAllNotDeleted();
    }

    @Test
    void findAllAccounts_withEmptyList_returnsNoContentStatus() {
        // given
        List<AccountDto> expected = Collections.emptyList();
        when(accountDatabaseService.findAllNotDeleted()).thenReturn(expected);

        // when
        ResponseEntity<List<AccountDto>> actual = accountController.findAllAccounts();

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(accountDatabaseService).findAllNotDeleted();
    }

    @Test
    void findAccountByUuid_success() {
        // given
        AccountDto expected = new AccountDto();
        when(accountDatabaseService.findById(uuid)).thenReturn(expected);

        // when
        ResponseEntity<AccountDto> actual = accountController.findAccountByUuid(String.valueOf(uuid));

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).findById(uuid);
    }

    @Test
    void findAllAccountsByStatus() {
        // given
        List<AccountDto> expected = List.of(new AccountDto(), new AccountDto());
        String status = AccountStatus.ACTIVE.name();
        when(accountDatabaseService.findAllByStatus(status)).thenReturn(expected);

        // when
        ResponseEntity<List<AccountDto>> actual = accountController.findAllAccountsByStatus(status);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).findAllByStatus(status);
    }

    @Test
    void updateAccount() {
        // given
        AccountDto expected = new AccountDto();

        // when
        ResponseEntity<AccountDto> actual = accountController.updateAccount(String.valueOf(uuid), expected);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).update(uuid, expected);
    }

    @Test
    void deleteAccount() {
        // when
        ResponseEntity<String> actual = accountController.deleteAccount(String.valueOf(uuid));

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        verify(accountDatabaseService).delete(uuid);
    }

    @Test
    void blockAllAccountsByClientUuid() {
        // when
        ResponseEntity<String> actual = accountController.blockAllAccountsByClientUuid(String.valueOf(uuid));

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        verify(accountDatabaseService).blockAccountsByClientUuid(uuid);
    }

    @Test
    void findAllAccountsByProductIdAndStatus() {
        // given
        List<AccountDto> expected = List.of(new AccountDto(), new AccountDto());
        ProductStatus status = ProductStatus.ACTIVE;
        when(accountDatabaseService
                .findAccountsByProductIdAndStatus(uuid, status)).thenReturn(expected);

        // when
        ResponseEntity<List<AccountDto>> actual = accountController
                .findAllAccountsByProductIdAndStatus(String.valueOf(uuid), String.valueOf(status));

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).findAccountsByProductIdAndStatus(uuid, status);
    }

    @Test
    void findAllAccountsByClientUuid() {
        // given
        List<AccountDto> expected = List.of(new AccountDto(), new AccountDto());
        when(accountDatabaseService.findAllDtoByClientId(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<AccountDto>> actual = accountController.findAllAccountsByClientUuid(String.valueOf(uuid));

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).findAllDtoByClientId(uuid);
    }
}