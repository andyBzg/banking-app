package org.crazymages.bankingspringproject.dto.account.mapper;

import org.crazymages.bankingspringproject.dto.account.AccountDto;
import org.crazymages.bankingspringproject.dto.account.mapper.AccountDtoMapper;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountDtoMapperTest {

    AccountDtoMapper accountDtoMapper;
    AccountDto accountDto;
    Account account1;

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
}
