package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.crazymages.bankingspringproject.service.utils.validator.ListValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountDatabaseServiceImplTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    EntityUpdateService<Account> accountUpdateService;
    @Mock
    ListValidator<Account> listValidator;

    @InjectMocks
    AccountDatabaseServiceImpl accountDatabaseService;

    Account account0;
    Account account1;
    UUID uuid;

    @BeforeEach
    void init() {
        account0 = new Account();
        account1 = new Account();
        uuid = UUID.randomUUID();
    }

    @Test
    void create_addNewAccountToUserRepository_success() {
        accountDatabaseService.create(account0);

        verify(accountRepository).save(account0);
    }

    @Test
    void findAll_returnAccountsFromRepository_success() {
        // given
        List<Account> expected = List.of(account0, account1);
        when(accountRepository.findAll()).thenReturn(expected);

        // when
        List<Account> actual = accountRepository.findAll();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void findAll_emptyList_success() {
        // given
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<Account> actual = accountDatabaseService.findAll();

        // then
        assertTrue(actual.isEmpty());
        verify(accountRepository).findAll();
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void findById_returnAccountFromRepository_success() {
        // given
        when(accountRepository.findById(uuid)).thenReturn(Optional.of(account0));

        // when
        Account result = accountDatabaseService.findById(uuid);

        // then
        assertEquals(account0, result);
    }

    @Test
    void findById_invalidUuid_throwsException() {
        when(accountRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> accountDatabaseService.findById(uuid));
        verify(accountRepository).findById(uuid);
    }

    @Test
    void findAllByStatus_success() {
        // given
        List<Account> expected = List.of(account0, account1);
        String status = "ACTIVE";
        when(accountRepository.findAccountsByStatus(AccountStatus.valueOf(status))).thenReturn(expected);
        when(listValidator.validate(expected)).thenReturn(expected);

        // when
        List<Account> actual = accountDatabaseService.findAllByStatus(status);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsByStatus(AccountStatus.valueOf(status));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void update_validAccount_success() {
        //given
        Account updatedAccount = account1;
        Account account = account0;
        when(accountRepository.findById(uuid)).thenReturn(Optional.of(account));
        when(accountUpdateService.update(account, updatedAccount)).thenReturn(updatedAccount);

        //when
        accountDatabaseService.update(uuid, updatedAccount);

        //then
        assertEquals(account, updatedAccount);
        verify(accountRepository).findById(uuid);
        verify(accountUpdateService).update(account, updatedAccount);
        verify(accountRepository).save(account);
    }

    @Test
    void delete_deleteUserFromUserRepository_success() {
        // given
        when(accountRepository.findById(uuid)).thenReturn(Optional.of(account0));

        // when
        accountDatabaseService.delete(uuid);

        // then
        verify(accountRepository).findById(uuid);
        verify(accountRepository).save(account0);
        assertTrue(account0.isDeleted());
    }

}