package org.crazymages.bankingspringproject.service.utils.updater.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountUpdateServiceImplTest {

    AccountUpdateServiceImpl accountUpdateService;
    Account account;

    @BeforeEach
    void setUp() {
        accountUpdateService = new AccountUpdateServiceImpl();
        account = new Account();
        account.setClientUuid(UUID.randomUUID());
        account.setName("Test Account");
        account.setType(AccountType.CURRENT);
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.valueOf(100));
        account.setCurrencyCode(CurrencyCode.EUR);
    }

    @Test
    void update_withValidFields_updatesAccountProperties() {
        // given
        Account accountUpdate = new Account();
        accountUpdate.setClientUuid(UUID.randomUUID());
        accountUpdate.setName("Updated Account");
        accountUpdate.setType(AccountType.SAVINGS);
        accountUpdate.setStatus(AccountStatus.BLOCKED);
        accountUpdate.setBalance(BigDecimal.valueOf(200));
        accountUpdate.setCurrencyCode(CurrencyCode.GBP);

        // when
        Account actual = accountUpdateService.update(account, accountUpdate);

        // then
        assertEquals(accountUpdate.getClientUuid(), actual.getClientUuid());
        assertEquals(accountUpdate.getName(), actual.getName());
        assertEquals(accountUpdate.getType(), actual.getType());
        assertEquals(accountUpdate.getStatus(), actual.getStatus());
        assertEquals(accountUpdate.getBalance(), actual.getBalance());
        assertEquals(accountUpdate.getCurrencyCode(), actual.getCurrencyCode());
    }

    @Test
    void updateProperties_accountUpdateWithNullFields_updatesOnlyExistentProperties() {
        // given
        Account accountUpdate = new Account();
        accountUpdate.setName("Updated Account");
        accountUpdate.setStatus(AccountStatus.BLOCKED);
        accountUpdate.setBalance(BigDecimal.valueOf(200));

        // when
        Account actual = accountUpdateService.updateProperties(account, accountUpdate);

        // then
        assertEquals(account.getClientUuid(), actual.getClientUuid());
        assertEquals(accountUpdate.getName(), actual.getName());
        assertEquals(account.getType(), actual.getType());
        assertEquals(accountUpdate.getStatus(), actual.getStatus());
        assertEquals(accountUpdate.getBalance(), actual.getBalance());
        assertEquals(account.getCurrencyCode(), actual.getCurrencyCode());
    }

    @Test
    void update_withNullAccount_doesNotUpdateAccountProperties_returnsNull() {
        // given
        Account accountUpdate = new Account();
        accountUpdate.setName("Updated Account");
        accountUpdate.setStatus(AccountStatus.BLOCKED);
        accountUpdate.setBalance(BigDecimal.valueOf(200));

        // when
        Account actual = accountUpdateService.update(null, accountUpdate);

        // then
        assertNull(actual);
    }

    @Test
    void update_withNullAccountUpdate_doesNotUpdateAccountProperties() {
        // given
        Account accountUpdate = null;

        // when
        Account actual = accountUpdateService.update(account, accountUpdate);

        // then
        assertEquals(account.getClientUuid(), actual.getClientUuid());
        assertEquals(account.getName(), actual.getName());
        assertEquals(account.getType(), actual.getType());
        assertEquals(account.getStatus(), actual.getStatus());
        assertEquals(account.getBalance(), actual.getBalance());
        assertEquals(account.getCurrencyCode(), actual.getCurrencyCode());
    }

    @Test
    void updateProperties_withNullAccountUpdate_throwsNullPointerException() {
        // given
        Account accountUpdate = null;

        // when, then
        assertThrows(NullPointerException.class, () -> accountUpdateService.updateProperties(account, accountUpdate));
    }
}
