package org.crazymages.bankingspringproject.dto.mapper.account;

import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountCreationMapperTest {

    AccountCreationMapper accountCreationMapper;
    AccountDto accountDto;
    Account account1;

    @BeforeEach
    void setUp() {
        accountCreationMapper = new AccountCreationMapper();
        accountDto = AccountDto.builder().build();
        account1 = new Account();

        account1.setName("Test Account");
        account1.setType(AccountType.CURRENT);
        account1.setCurrencyCode(CurrencyCode.EUR);
    }

    @Test
    void mapEntityToDto_validAccount_success() {
        // when
        AccountDto accountDto = accountCreationMapper.mapEntityToDto(account1);

        // then
        assertEquals(account1.getName(), accountDto.getName());
        assertEquals(account1.getType().toString(), accountDto.getType());
        assertEquals(account1.getCurrencyCode().toString(), accountDto.getCurrencyCode());
    }
    @Test
    void mapEntityToDto_nullAccount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountCreationMapper.mapEntityToDto(null));
    }

    @Test
    void mapEntityToDto_accountWithNullProperties_returnsAccountDtoWithNullProperties() {
        // given
        Account account = new Account();

        // when
        AccountDto accountDto = accountCreationMapper.mapEntityToDto(account);

        // then
        assertNull(accountDto.getName());
        assertNull(accountDto.getType());
        assertNull(accountDto.getCurrencyCode());
    }

    @Test
    void mapDtoToEntity_validAccountDto_success() {
        // given
        accountDto.setName("Test AccountDto");
        accountDto.setType("CURRENT");
        accountDto.setCurrencyCode("EUR");

        // when
        Account account = accountCreationMapper.mapDtoToEntity(accountDto);

        // then
        assertFalse(account.isDeleted());
        assertEquals(accountDto.getName(), account.getName());
        assertEquals(AccountType.valueOf(accountDto.getType()), account.getType());
        assertEquals(CurrencyCode.valueOf(accountDto.getCurrencyCode()), account.getCurrencyCode());
    }

    @Test
    void mapDtoToEntity_nullAccountDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountCreationMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingAccountDtoProperties_returnsAccountWithNullProperties() {
        // when
        Account account = accountCreationMapper.mapDtoToEntity(accountDto);

        // then
        assertFalse(account.isDeleted());
        assertNull(account.getName());
        assertNull(account.getType());
        assertNull(account.getCurrencyCode());
    }
}