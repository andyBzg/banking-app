package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.AccountDTO;
import org.crazymages.bankingspringproject.dto.AgreementDTO;
import org.crazymages.bankingspringproject.entity.*;
import org.crazymages.bankingspringproject.entity.enums.*;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.crazymages.bankingspringproject.service.utils.creator.AgreementCreator;
import org.crazymages.bankingspringproject.service.utils.mapper.AccountDTOMapper;
import org.crazymages.bankingspringproject.service.utils.mapper.AgreementDTOMapper;
import org.crazymages.bankingspringproject.service.utils.matcher.ProductTypeMatcher;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
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
    @Mock
    AccountDTOMapper accountDTOMapper;
    @Mock
    EntityUpdateService<Account> accountUpdateService;
    @Mock
    ProductDatabaseService productDatabaseService;
    @Mock
    AgreementDatabaseService agreementDatabaseService;
    @Mock
    AgreementCreator agreementCreator;
    @Mock
    AgreementDTOMapper agreementDTOMapper;
    @Mock
    ProductTypeMatcher productTypeMatcher;

    @InjectMocks
    AccountDatabaseServiceImpl accountDatabaseService;

    Account account1;
    Account account2;
    AccountDTO accountDto1;
    AccountDTO accountDto2;
    UUID clientUuid;
    UUID uuid;

    @BeforeEach
    void init() {
        account1 = new Account();
        account2 = new Account();
        clientUuid = UUID.randomUUID();
        uuid = UUID.randomUUID();
        accountDto1 = new AccountDTO(
                UUID.fromString("30348dce-45f7-4e19-aa08-3ed77a8f7ac3"),
                clientUuid,
                "First AccountDto",
                AccountType.CURRENT,
                AccountStatus.ACTIVE,
                BigDecimal.valueOf(0),
                CurrencyCode.EUR
        );
        accountDto2 = new AccountDTO(
                UUID.fromString("684b798b-8e6f-4c6a-bad1-91c2e37d4d48"),
                clientUuid,
                "Second AccountDto",
                AccountType.CURRENT,
                AccountStatus.ACTIVE,
                BigDecimal.valueOf(0),
                CurrencyCode.EUR
        );
    }

    @Test
    void create_success() {
        // given
        when(accountDTOMapper.mapToAccount(accountDto1)).thenReturn(account1);

        // when
        accountDatabaseService.create(accountDto1);

        // then
        verify(accountDTOMapper).mapToAccount(accountDto1);
        verify(accountRepository).save(account1);
    }

    @Test
    void create_withClientId_success() {
        // given
        CurrencyCode currencyCode = accountDto1.getCurrencyCode();
        ProductStatus status = ProductStatus.ACTIVE;
        ProductType type = productTypeMatcher.matchTypes(accountDto1.getType());
        Product product = new Product();
        Agreement agreement = new Agreement();
        AgreementDTO agreementDTO = new AgreementDTO();

        when(accountDTOMapper.mapToAccount(accountDto1)).thenReturn(account1);
        when(accountRepository.save(account1)).thenReturn(account1);
        when(productDatabaseService.findProductByTypeAndStatusAndCurrencyCode(type, status, currencyCode)).thenReturn(product);
        when(agreementCreator.apply(account1.getUuid(), product)).thenReturn(agreement);
        when(agreementDTOMapper.mapToAgreementDTO(agreement)).thenReturn(agreementDTO);


        // when
        accountDatabaseService.create(accountDto1, clientUuid);


        // then
        verify(accountDTOMapper).mapToAccount(accountDto1);
        verify(accountRepository).save(account1);
        verify(productDatabaseService).findProductByTypeAndStatusAndCurrencyCode(type, status, currencyCode);
        verify(agreementCreator).apply(account1.getUuid(), product);
        verify(agreementDatabaseService).create(agreementDTO);
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<Account> accounts = List.of(account1, account2);
        List<AccountDTO> expected = List.of(accountDto1, accountDto2);

        when(accountRepository.findAllNotDeleted()).thenReturn(accounts);
        when(accountDTOMapper.getListOfAccountDTOs(anyList())).thenReturn(expected);


        // when
        List<AccountDTO> actual = accountDatabaseService.findAllNotDeleted();


        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAllNotDeleted();
        verify(accountDTOMapper).getListOfAccountDTOs(anyList());
    }

    @Test
    void findAllDeleted_success() {
        // given
        List<Account> accounts = List.of(account1, account2);
        List<AccountDTO> expected = List.of(accountDto1, accountDto2);

        when(accountRepository.findAllDeleted()).thenReturn(accounts);
        when(accountDTOMapper.getListOfAccountDTOs(anyList())).thenReturn(expected);


        // when
        List<AccountDTO> actual = accountDatabaseService.findDeletedAccounts();


        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAllDeleted();
        verify(accountDTOMapper).getListOfAccountDTOs(anyList());
    }

    @Test
    void findById_returnAccountFromRepository_success() {
        // given
        when(accountRepository.findById(uuid)).thenReturn(Optional.ofNullable(account1));
        when(accountDTOMapper.mapToAccountDTO(account1)).thenReturn(accountDto1);

        // when
        AccountDTO actual = accountDatabaseService.findById(uuid);

        // then
        assertEquals(accountDto1, actual);
        verify(accountRepository).findById(uuid);
        verify(accountDTOMapper).mapToAccountDTO(account1);
    }

    @Test
    void findById_invalidUuid_throwsException() {
        UUID invalidUuid = UUID.randomUUID();
        when(accountRepository.findById(invalidUuid)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> accountDatabaseService.findById(invalidUuid));
        verify(accountRepository).findById(invalidUuid);
    }

    @Test
    void findAllByStatus_success() {
        // given
        List<AccountDTO> expected = List.of(accountDto1, accountDto2);
        List<Account> accounts = List.of(account1, account2);
        String status = "ACTIVE";

        when(accountRepository.findAccountsByStatus(AccountStatus.valueOf(status))).thenReturn(accounts);
        when(accountDTOMapper.getListOfAccountDTOs(accounts)).thenReturn(expected);


        // when
        List<AccountDTO> actual = accountDatabaseService.findAllByStatus(status);


        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsByStatus(AccountStatus.valueOf(status));
        verify(accountDTOMapper).getListOfAccountDTOs(accounts);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void update_validAccount_success() {
        //given
        AccountDTO updatedAccountDTO = accountDto1;
        Account updatedAccount = account1;
        Account account = account1;

        when(accountDTOMapper.mapToAccount(updatedAccountDTO)).thenReturn(updatedAccount);
        when(accountRepository.findById(uuid)).thenReturn(Optional.of(account));
        when(accountUpdateService.update(account, updatedAccount)).thenReturn(updatedAccount);


        //when
        accountDatabaseService.update(uuid, updatedAccountDTO);


        //then
        verify(accountDTOMapper).mapToAccount(updatedAccountDTO);
        verify(accountRepository).findById(uuid);
        verify(accountUpdateService).update(account, updatedAccount);
        verify(accountRepository).save(account);
    }

    @Test
    void delete_deleteUserFromUserRepository_success() {
        // given
        when(accountRepository.findById(uuid)).thenReturn(Optional.of(account1));

        // when
        accountDatabaseService.delete(uuid);

        // then
        verify(accountRepository).findById(uuid);
        verify(accountRepository).save(account1);
        assertTrue(account1.isDeleted());
    }

    @Test
    void blockAccountsByClientUuid_success() {
        // when
        accountDatabaseService.blockAccountsByClientUuid(uuid);

        // then
        verify(accountRepository).blockAccountsByClientUuid(uuid);
    }

    @Test
    void findAccountsByProductIdAndStatus_success() {
        // given
        List<Account> accounts = List.of(account1, account2);
        List<AccountDTO> expected = List.of(accountDto1, accountDto2);
        UUID productUuid = UUID.randomUUID();
        ProductStatus status = ProductStatus.ACTIVE;

        when(accountRepository.findAccountsWhereProductIdAndStatusIs(productUuid, status)).thenReturn(accounts);
        when(accountDTOMapper.getListOfAccountDTOs(accounts)).thenReturn(expected);


        // when
        List<AccountDTO> actual = accountDatabaseService.findAccountsByProductIdAndStatus(productUuid, status);


        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsWhereProductIdAndStatusIs(productUuid, status);
        verify(accountDTOMapper).getListOfAccountDTOs(accounts);
    }

    @Test
    void findAllByClientId_success() {
        // given
        List<Account> accounts = List.of(account1, account2);
        List<AccountDTO> expected = List.of(accountDto1, accountDto2);
        when(accountRepository.findAccountsByClientUuid(clientUuid)).thenReturn(accounts);
        when(accountDTOMapper.getListOfAccountDTOs(accounts)).thenReturn(expected);

        // when
        List<AccountDTO> actual = accountDatabaseService.findAllByClientId(clientUuid);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountsByClientUuid(clientUuid);
        verify(accountDTOMapper).getListOfAccountDTOs(accounts);
    }

    @Test
    void findCurrentByClientId_success() {
        // given
        Account expected = account1;
        AccountType type = AccountType.CURRENT;
        when(accountRepository.findAccountByClientUuidAndType(clientUuid, type)).thenReturn(Optional.ofNullable(account1));

        // when
        Account actual = accountDatabaseService.findCurrentByClientId(clientUuid);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountByClientUuidAndType(clientUuid, type);
    }

    @Test
    void findSavingsByClientId_success() {
        Account expected = account2;
        AccountType type = AccountType.SAVINGS;
        when(accountRepository.findAccountByClientUuidAndType(clientUuid, type)).thenReturn(Optional.ofNullable(account2));

        // when
        Account actual = accountDatabaseService.findSavingsByClientId(clientUuid);

        // then
        assertEquals(expected, actual);
        verify(accountRepository).findAccountByClientUuidAndType(clientUuid, type);
    }
}
