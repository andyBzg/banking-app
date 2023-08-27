package org.crazymages.bankingspringproject.dto.account.mapper;

import org.crazymages.bankingspringproject.dto.account.AccountDto;
import org.crazymages.bankingspringproject.dto.account.mapper.AccountUpdateMapper;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountUpdateMapperTest {

    AccountUpdateMapper accountUpdateMapper;
    AccountDto accountDto;
    Account account1;

    @BeforeEach
    void setUp() {
        accountUpdateMapper = new AccountUpdateMapper();
        accountDto = AccountDto.builder().build();
        account1 = new Account();

        account1.setName("Test Account");
        account1.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    void mapEntityToDto_validAccount_success() {
        // when
        AccountDto accountDto = accountUpdateMapper.mapEntityToDto(account1);

        // then
        assertEquals(account1.getName(), accountDto.getName());
        assertEquals(account1.getStatus().toString(), accountDto.getStatus());
    }

    @Test
    void mapEntityToDto_nullAccount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountUpdateMapper.mapEntityToDto(null));
    }

    @Test
    void mapEntityToDto_accountWithNullProperties_returnsAccountDtoWithNullProperties() {
        // given
        Account account = new Account();

        // when
        AccountDto accountDto = accountUpdateMapper.mapEntityToDto(account);

        // then
        assertNull(accountDto.getName());
        assertNull(accountDto.getStatus());
    }

    @Test
    void mapDtoToEntity_validAccountDto_success() {
        // given
        accountDto.setName("Test AccountDto");
        accountDto.setStatus("ACTIVE");

        // when
        Account account = accountUpdateMapper.mapDtoToEntity(accountDto);

        // then
        assertFalse(account.isDeleted());
        assertEquals(accountDto.getName(), account.getName());
        assertEquals(AccountStatus.valueOf(accountDto.getStatus()), account.getStatus());
    }

    @Test
    void mapDtoToEntity_nullAccountDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountUpdateMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingAccountDtoProperties_returnsAccountWithNullProperties() {
        // when
        Account account = accountUpdateMapper.mapDtoToEntity(accountDto);

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
