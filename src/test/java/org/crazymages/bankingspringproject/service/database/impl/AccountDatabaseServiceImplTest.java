package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountDatabaseServiceImplTest {

    @Mock
    AccountRepository accountRepository;

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
    void findAllByStatus() {
        // given
        List<Account> expected = List.of(account0, account1);
        when(accountRepository.findAccountsByStatus(AccountStatus.ACTIVE)).thenReturn(expected);

        // when
        List<Account> actual = accountDatabaseService.findAllByStatus(AccountStatus.ACTIVE.name());

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsByStatus(AccountStatus.ACTIVE);
    }

    @Test
    void update_updateAccountInRepositoryIfPresent_success() {
        // given
        Account expected = account1;
        expected.setName("test");
        expected.setBalance(new BigDecimal(500));
        when(accountRepository.findById(uuid)).thenReturn(Optional.of(account0));

        // when
        accountDatabaseService.update(uuid, expected);
        Account actual = accountDatabaseService.findById(uuid);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).save(expected);
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