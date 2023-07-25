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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    AccountDatabaseService accountDatabaseService;

    @InjectMocks
    AccountController accountController;

    String uuid;

    @BeforeEach
    void setUp() {
        uuid = "7bcf30be-8c6e-4e10-a73b-706849fc94dc";
    }

    @Test
    void createAccount_success() {
        // when
        AccountDto accountDto = AccountDto.builder().build();
        AccountDto createdAccountDto = AccountDto.builder().build();

        // when
        ResponseEntity<AccountDto> actual = accountController.createAccount(accountDto);

        // then
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(createdAccountDto, actual.getBody());
        verify(accountDatabaseService).create(accountDto);
    }

    @Test
    void createAccount_emptyAccountDto_savesNoData() {
        // when
        ResponseEntity<AccountDto> actual = accountController.createAccount(null);

        // then
        assertNull(actual.getBody());
        verify(accountDatabaseService, never()).create(any(AccountDto.class));
    }

    @Test
    void createAccount_withClientUuid_success() {
        // when
        AccountDto accountDto = AccountDto.builder().build();
        AccountDto createdAccountDto = AccountDto.builder().build();

        // when
        ResponseEntity<AccountDto> actual = accountController.createAccount(accountDto, uuid);

        // then
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(createdAccountDto, actual.getBody());
        verify(accountDatabaseService).create(accountDto, uuid);
    }

    @Test
    void findAllAccounts_success() {
        // given
        List<AccountDto> expected = List.of(AccountDto.builder().build(), AccountDto.builder().build());
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
        AccountDto expected = AccountDto.builder().build();
        when(accountDatabaseService.findDtoById(uuid)).thenReturn(expected);

        // when
        ResponseEntity<AccountDto> actual = accountController.findAccountByUuid(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).findDtoById(uuid);
    }

    @Test
    void findAllAccountsByStatus_success() {
        // given
        List<AccountDto> expected = List.of(AccountDto.builder().build(), AccountDto.builder().build());
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
    void updateAccountDto_success() {
        // given
        AccountDto expected = AccountDto.builder().build();

        // when
        ResponseEntity<AccountDto> actual = accountController.updateAccount(uuid, expected);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).updateAccountDto(uuid, expected);
    }

    @Test
    void deleteAccount_success() {
        // when
        ResponseEntity<String> actual = accountController.deleteAccount(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        verify(accountDatabaseService).delete(uuid);
    }

    @Test
    void blockAllAccountsByClientUuid_success() {
        // when
        ResponseEntity<String> actual = accountController.blockAllAccountsByClientUuid(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        verify(accountDatabaseService).blockAccountsByClientUuid(uuid);
    }

    @Test
    void findAllAccountsByProductIdAndStatus_success() {
        // given
        List<AccountDto> expected = List.of(AccountDto.builder().build(), AccountDto.builder().build());
        String status = ProductStatus.ACTIVE.name();
        when(accountDatabaseService
                .findAccountsByProductIdAndStatus(uuid, status)).thenReturn(expected);

        // when
        ResponseEntity<List<AccountDto>> actual = accountController
                .findAllAccountsByProductIdAndStatus(uuid, status);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).findAccountsByProductIdAndStatus(uuid, status);
    }

    @Test
    void findAllAccountsByClientUuid_success() {
        // given
        List<AccountDto> expected = List.of(AccountDto.builder().build(), AccountDto.builder().build());
        when(accountDatabaseService.findAllDtoByClientId(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<AccountDto>> actual = accountController.findAllAccountsByClientUuid(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(accountDatabaseService).findAllDtoByClientId(uuid);
    }
}
