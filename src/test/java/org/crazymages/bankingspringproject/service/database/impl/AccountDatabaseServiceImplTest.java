package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.account.AccountDto;
import org.crazymages.bankingspringproject.dto.agreement.AgreementDto;
import org.crazymages.bankingspringproject.dto.account.AccountCreationDto;
import org.crazymages.bankingspringproject.dto.account.mapper.AccountCreationMapper;
import org.crazymages.bankingspringproject.dto.account.mapper.AccountUpdateMapper;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.crazymages.bankingspringproject.service.utils.initializer.AgreementInitializer;
import org.crazymages.bankingspringproject.dto.account.mapper.AccountDtoMapper;
import org.crazymages.bankingspringproject.dto.agreement.mapper.AgreementDtoMapper;
import org.crazymages.bankingspringproject.service.utils.matcher.ProductTypeMatcher;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDatabaseServiceImplTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountDtoMapper accountDtoMapper;
    @Mock
    AccountCreationMapper accountCreationMapper;
    @Mock
    AccountUpdateMapper accountUpdateMapper;
    @Mock
    EntityUpdateService<Account> accountUpdateService;
    @Mock
    ProductDatabaseService productDatabaseService;
    @Mock
    AgreementDatabaseService agreementDatabaseService;
    @Mock
    AgreementInitializer agreementInitializer;
    @Mock
    AgreementDtoMapper agreementDTOMapper;
    @Mock
    ProductTypeMatcher productTypeMatcher;

    @InjectMocks
    AccountDatabaseServiceImpl accountDatabaseService;

    Account account1;
    Account account2;
    AccountDto accountDto1;
    AccountDto accountDto2;
    AccountCreationDto accountCreationDto;
    String clientUuid;
    String uuid;

    @BeforeEach
    void init() {
        account1 = new Account();
        account2 = new Account();
        clientUuid = "a9b2090a-9585-44af-8722-c6e51590a0af";
        uuid = "1510e3a7-76ba-464a-a896-4c645c1bf2e7";
        accountDto1 = AccountDto.builder().build();
        accountDto2 = AccountDto.builder().build();
        accountCreationDto = AccountCreationDto.builder().build();
    }

    @Test
    void create_withClientId_success() {
        // given
        Product product = new Product();
        ProductType type = ProductType.CURRENT_ACCOUNT;
        ProductStatus status = ProductStatus.ACTIVE;
        CurrencyCode currencyCode = account1.getCurrencyCode();

        Agreement agreement = new Agreement();
        AgreementDto agreementDto = AgreementDto.builder().build();

        when(accountCreationMapper.mapDtoToEntity(accountCreationDto)).thenReturn(account1);
        when(productTypeMatcher.matchTypes(account1.getType())).thenReturn(type);
        when(productDatabaseService.findProductByTypeAndStatusAndCurrencyCode(type, status, currencyCode))
                .thenReturn(product);
        when(agreementInitializer.initializeAgreement(account1.getUuid(), product)).thenReturn(agreement);
        when(agreementDTOMapper.mapEntityToDto(agreement)).thenReturn(agreementDto);


        // when
        accountDatabaseService.create(accountCreationDto, uuid);


        // then
        assertEquals(UUID.fromString(uuid), account1.getClientUuid());
        verify(accountCreationMapper).mapDtoToEntity(accountCreationDto);
        verify(accountRepository).save(account1);
        verify(productTypeMatcher).matchTypes(account1.getType());
        verify(productDatabaseService).findProductByTypeAndStatusAndCurrencyCode(type, status, currencyCode);
        verify(agreementInitializer).initializeAgreement(account1.getUuid(), product);
        verify(agreementDTOMapper).mapEntityToDto(agreement);
        verify(agreementDatabaseService).create(agreementDto);
    }

    @Test
    void create_nullAccountDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountDatabaseService.create(null, uuid));
    }


    @Test
    void create_nullClientUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountDatabaseService.create(accountCreationDto, null));
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<Account> accounts = List.of(account1, account2);
        List<AccountDto> expected = List.of(accountDto1, accountDto2);

        when(accountRepository.findAllNotDeleted()).thenReturn(accounts);
        when(accountDtoMapper.mapEntityToDto(account1)).thenReturn(accountDto1);
        when(accountDtoMapper.mapEntityToDto(account2)).thenReturn(accountDto2);


        // when
        List<AccountDto> actual = accountDatabaseService.findAllNotDeleted();


        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAllNotDeleted();
        verify(accountDtoMapper, times(2)).mapEntityToDto(any(Account.class));
    }

    @Test
    void findAllDeleted_success() {
        // given
        List<Account> accounts = List.of(account1, account2);
        List<AccountDto> expected = List.of(accountDto1, accountDto2);

        when(accountRepository.findAllDeleted()).thenReturn(accounts);
        when(accountDtoMapper.mapEntityToDto(account1)).thenReturn(accountDto1);
        when(accountDtoMapper.mapEntityToDto(account2)).thenReturn(accountDto2);


        // when
        List<AccountDto> actual = accountDatabaseService.findDeletedAccounts();


        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAllDeleted();
        verify(accountDtoMapper, times(2)).mapEntityToDto(any(Account.class));
    }

    @Test
    void findById_returnAccountFromRepository_success() {
        // given
        UUID uuid = UUID.fromString("a9b2090a-9585-44af-8722-c6e51590a0af");
        when(accountRepository.findById(uuid)).thenReturn(Optional.ofNullable(account1));
        when(accountDtoMapper.mapEntityToDto(account1)).thenReturn(accountDto1);

        // when
        AccountDto actual = accountDatabaseService.findDtoById(String.valueOf(uuid));

        // then
        assertEquals(accountDto1, actual);
        verify(accountRepository).findById(uuid);
        verify(accountDtoMapper).mapEntityToDto(account1);
    }

    @Test
    void findById_invalidUuid_throwsException() {
        String invalidUuid = "d358838e-1134-4101-85ac-5d99e8debfae";
        when(accountRepository.findById(UUID.fromString(invalidUuid))).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> accountDatabaseService.findDtoById(invalidUuid));
        verify(accountRepository).findById(UUID.fromString(invalidUuid));
    }

    @Test
    void findById_nullUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountDatabaseService.findDtoById(null));
    }

    @Test
    void findDtoById_invalidUuid_throwsIllegalArgumentException() {
        // given
        String invalidUuid = "invalid_uuid";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> accountDatabaseService.findDtoById(invalidUuid));
    }

    @Test
    void findDtoById_nullUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountDatabaseService.findDtoById(null));
    }

    @Test
    void findAllByStatus_success() {
        // given
        List<AccountDto> expected = List.of(accountDto1, accountDto2);
        List<Account> accounts = List.of(account1, account2);
        String status = "ACTIVE";

        when(accountRepository.findAccountsByStatus(AccountStatus.valueOf(status))).thenReturn(accounts);
        when(accountDtoMapper.mapEntityToDto(account1)).thenReturn(accountDto1);
        when(accountDtoMapper.mapEntityToDto(account2)).thenReturn(accountDto2);


        // when
        List<AccountDto> actual = accountDatabaseService.findAllByStatus(status);


        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsByStatus(AccountStatus.valueOf(status));
        verify(accountDtoMapper, times(2)).mapEntityToDto(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void updateAccountDto_validAccount_success() {
        //given
        AccountDto updatedAccountDto = accountDto1;
        Account updatedAccount = account1;
        Account account = account1;
        UUID uuid = UUID.fromString("a9b2090a-9585-44af-8722-c6e51590a0af");

        when(accountUpdateMapper.mapDtoToEntity(updatedAccountDto)).thenReturn(updatedAccount);
        when(accountRepository.findById(uuid)).thenReturn(Optional.of(account));
        when(accountUpdateService.update(account, updatedAccount)).thenReturn(updatedAccount);


        //when
        accountDatabaseService.updateAccountDto(String.valueOf(uuid), updatedAccountDto);


        //then
        verify(accountUpdateMapper).mapDtoToEntity(updatedAccountDto);
        verify(accountRepository).findById(uuid);
        verify(accountUpdateService).update(account, updatedAccount);
        verify(accountRepository).save(account);
    }

    @Test
    void update_nullAccount_throwsIllegalArgumentException() {
        UUID uuid = UUID.fromString("a9b2090a-9585-44af-8722-c6e51590a0af");

        assertThrows(IllegalArgumentException.class, () -> accountDatabaseService.update(uuid, null));
    }

    @Test
    void update_nullUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> accountDatabaseService.update(null, account1));
    }

    @Test
    void delete_deleteUserFromUserRepository_success() {
        // given
        UUID uuid = UUID.fromString("a9b2090a-9585-44af-8722-c6e51590a0af");
        when(accountRepository.findById(uuid)).thenReturn(Optional.of(account1));

        // when
        accountDatabaseService.delete(String.valueOf(uuid));

        // then
        verify(accountRepository).findById(uuid);
        verify(accountRepository).save(account1);
        assertTrue(account1.isDeleted());
    }

    @Test
    void blockAccountsByClientUuid_success() {
        // when
        UUID uuid = UUID.fromString("a9b2090a-9585-44af-8722-c6e51590a0af");
        accountDatabaseService.blockAccountsByClientUuid(String.valueOf(uuid));

        // then
        verify(accountRepository).blockAccountsByClientUuid(uuid);
    }

    @Test
    void findAccountsByProductIdAndStatus_success() {
        // given
        List<Account> accounts = List.of(account1, account2);
        List<AccountDto> expected = List.of(accountDto1, accountDto2);
        UUID productUuid = UUID.randomUUID();
        ProductStatus status = ProductStatus.ACTIVE;

        when(accountRepository.findAccountsWhereProductIdAndStatusIs(productUuid, status)).thenReturn(accounts);
        when(accountDtoMapper.mapEntityToDto(account1)).thenReturn(accountDto1);
        when(accountDtoMapper.mapEntityToDto(account2)).thenReturn(accountDto2);


        // when
        List<AccountDto> actual = accountDatabaseService
                .findAccountsByProductIdAndStatus(String.valueOf(productUuid), String.valueOf(status));


        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsWhereProductIdAndStatusIs(productUuid, status);
        verify(accountDtoMapper, times(2)).mapEntityToDto(any(Account.class));
    }

    @Test
    void findAllDtoByClientId_success() {
        // given
        UUID clientUuid = UUID.fromString("1510e3a7-76ba-464a-a896-4c645c1bf2e7");
        List<Account> accounts = List.of(account1, account2);
        List<AccountDto> expected = List.of(accountDto1, accountDto2);
        when(accountRepository.findAccountsByClientUuid(clientUuid)).thenReturn(accounts);
        when(accountDtoMapper.mapEntityToDto(account1)).thenReturn(accountDto1);
        when(accountDtoMapper.mapEntityToDto(account2)).thenReturn(accountDto2);

        // when
        List<AccountDto> actual = accountDatabaseService.findAllDtoByClientId(String.valueOf(clientUuid));

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsByClientUuid(clientUuid);
        verify(accountDtoMapper, times(2)).mapEntityToDto(any(Account.class));
    }

    @Test
    void findAllByClientId_success() {
        // given
        List<Account> expected = List.of(account1, account2);
        UUID clientUuid = UUID.fromString("1510e3a7-76ba-464a-a896-4c645c1bf2e7");
        when(accountRepository.findAccountsByClientUuid(clientUuid)).thenReturn(List.of(account1, account2));

        // when
        List<Account> actual = accountDatabaseService.findAllByClientId(clientUuid);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsByClientUuid(clientUuid);
    }

    @Test
    void findCurrentByClientId_success() {
        // given
        Account expected = account1;
        AccountType type = AccountType.CURRENT;
        UUID clientUuid = UUID.fromString("1510e3a7-76ba-464a-a896-4c645c1bf2e7");
        when(accountRepository.findAccountByClientUuidAndType(clientUuid, type)).thenReturn(Optional.ofNullable(account1));

        // when
        Account actual = accountDatabaseService.findCurrentByClientId(clientUuid);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountByClientUuidAndType(clientUuid, type);
    }

    @Test
    void findSavingsByClientId_success() {
        // given
        Account expected = account2;
        AccountType type = AccountType.SAVINGS;
        UUID clientUuid = UUID.fromString("1510e3a7-76ba-464a-a896-4c645c1bf2e7");
        when(accountRepository.findAccountByClientUuidAndType(clientUuid, type)).thenReturn(Optional.ofNullable(account2));

        // when
        Account actual = accountDatabaseService.findSavingsByClientId(clientUuid);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountByClientUuidAndType(clientUuid, type);
    }

    @Test
    void findAccountsByProductTypeAndStatus_success() {
        // given
        ProductType type = ProductType.DEPOSIT_ACCOUNT;
        ProductStatus status = ProductStatus.ACTIVE;
        List<Account> expected = List.of(account1, account2);
        when(accountRepository.findAccountsWhereProductTypeIsAndProductStatusIs(type, status))
                .thenReturn(List.of(account1, account2));

        // when
        List<Account> actual = accountDatabaseService.findAccountsByProductTypeAndStatus(type, status);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsWhereProductTypeIsAndProductStatusIs(type, status);
    }
}
