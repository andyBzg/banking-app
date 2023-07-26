package org.crazymages.bankingspringproject.dto.mapper.account;

import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.dto.mapper.account.AccountDtoMapper;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountDtoMapperTest {

    AccountDtoMapper accountDtoMapper;
    AccountDto accountDto;
    Account account1;
    Account account2;

    @BeforeEach
    void setUp() {
        accountDtoMapper = new AccountDtoMapper();
        accountDto = AccountDto.builder().build();

        account1 = new Account();
        account1.setUuid(UUID.randomUUID());
        account1.setClientUuid(UUID.randomUUID());
        account1.setName("Test Account");
        account1.setType(AccountType.CURRENT);
        account1.setStatus(AccountStatus.ACTIVE);
        account1.setBalance(BigDecimal.valueOf(100));
        account1.setCurrencyCode(CurrencyCode.EUR);

        account2 = new Account();
        account2.setUuid(UUID.randomUUID());
        account2.setClientUuid(UUID.randomUUID());
        account2.setName("Test Account 2");
        account2.setType(AccountType.SAVINGS);
        account2.setStatus(AccountStatus.BLOCKED);
        account2.setBalance(BigDecimal.valueOf(200));
        account2.setCurrencyCode(CurrencyCode.USD);
    }

    @Test
    void mapEntityToDto_validAccount_success() {
        // when
        AccountDto accountDto = accountDtoMapper.mapEntityToDto(account1);

        // then
        assertEquals(account1.getClientUuid().toString(), accountDto.getClientUuid());
        assertEquals(account1.getName(), accountDto.getName());
        assertEquals(account1.getType().toString(), accountDto.getType());
        assertEquals(account1.getStatus().toString(), accountDto.getStatus());
        assertEquals(account1.getBalance(), accountDto.getBalance());
        assertEquals(account1.getCurrencyCode().toString(), accountDto.getCurrencyCode());
    }

    @Test
    void mapEntityToDto_nullAccount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapEntityToDto_accountWithNullProperties_returnsAccountDtoWithNullProperties() {
        // given
        Account account = new Account();

        // when
        AccountDto accountDto = accountDtoMapper.mapEntityToDto(account);

        // then
        assertNull(accountDto.getClientUuid());
        assertNull(accountDto.getName());
        assertNull(accountDto.getType());
        assertNull(accountDto.getStatus());
        assertNull(accountDto.getBalance());
        assertNull(accountDto.getCurrencyCode());
    }

    @Test
    void mapDtoToEntity_validAccountDto_success() {
        // given
        accountDto.setClientUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");
        accountDto.setName("Test AccountDto");
        accountDto.setType("CURRENT");
        accountDto.setStatus("ACTIVE");
        accountDto.setBalance(BigDecimal.valueOf(100));
        accountDto.setCurrencyCode("EUR");

        // when
        Account account = accountDtoMapper.mapDtoToEntity(accountDto);

        // then
        assertFalse(account.isDeleted());
        assertEquals(UUID.fromString(accountDto.getClientUuid()), account.getClientUuid());
        assertEquals(accountDto.getName(), account.getName());
        assertEquals(AccountType.valueOf(accountDto.getType()), account.getType());
        assertEquals(AccountStatus.valueOf(accountDto.getStatus()), account.getStatus());
        assertEquals(accountDto.getBalance(), account.getBalance());
        assertEquals(CurrencyCode.valueOf(accountDto.getCurrencyCode()), account.getCurrencyCode());
    }

    @Test
    void mapDtoToEntity_nullAccountDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingAccountDtoProperties_returnsAccountWithNullProperties() {
        // when
        Account account = accountDtoMapper.mapDtoToEntity(accountDto);

        // then
        assertFalse(account.isDeleted());
        assertNull(account.getClientUuid());
        assertNull(account.getName());
        assertNull(account.getType());
        assertNull(account.getStatus());
        assertNull(account.getBalance());
        assertNull(account.getCurrencyCode());
    }

    @Test
    void getDtoList_validAccountList_success() {
        // given
        List<Account> accountList = List.of(account1, account2);

        // when
        List<AccountDto> actual = accountDtoMapper.getDtoList(accountList);

        // then
        assertEquals(2, actual.size());

        AccountDto accountDto1 = actual.get(0);
        assertEquals(account1.getClientUuid().toString(), accountDto1.getClientUuid());
        assertEquals(account1.getName(), accountDto1.getName());
        assertEquals(account1.getType().toString(), accountDto1.getType());
        assertEquals(account1.getStatus().toString(), accountDto1.getStatus());
        assertEquals(account1.getBalance(), accountDto1.getBalance());
        assertEquals(account1.getCurrencyCode().toString(), accountDto1.getCurrencyCode());

        AccountDto accountDto2 = actual.get(1);
        assertEquals(account2.getClientUuid().toString(), accountDto2.getClientUuid());
        assertEquals(account2.getName(), accountDto2.getName());
        assertEquals(account2.getType().toString(), accountDto2.getType());
        assertEquals(account2.getStatus().toString(), accountDto2.getStatus());
        assertEquals(account2.getBalance(), accountDto2.getBalance());
        assertEquals(account2.getCurrencyCode().toString(), accountDto2.getCurrencyCode());
    }

    @Test
    void getDtoList_nullAccountList_throwsDataNotFoundException() {
        assertThrows(DataNotFoundException.class, () -> accountDtoMapper.getDtoList(null));
    }

    @Test
    void getDtoList_emptyAccountList_returnsEmptyList() {
        // given
        List<Account> accountList = Collections.emptyList();

        // when
        List<AccountDto> actual = accountDtoMapper.getDtoList(accountList);

        // then
        assertTrue(actual.isEmpty());
    }
}
