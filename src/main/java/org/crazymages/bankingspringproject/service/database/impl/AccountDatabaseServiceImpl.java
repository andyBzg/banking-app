package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.mapper.account.AccountCreationMapper;
import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.dto.AgreementDto;
import org.crazymages.bankingspringproject.dto.mapper.account.AccountUpdateMapper;
import org.crazymages.bankingspringproject.entity.*;
import org.crazymages.bankingspringproject.entity.enums.*;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.crazymages.bankingspringproject.service.utils.initializer.AgreementInitializer;
import org.crazymages.bankingspringproject.dto.mapper.account.AccountDtoMapper;
import org.crazymages.bankingspringproject.dto.mapper.agreement.AgreementDtoMapper;
import org.crazymages.bankingspringproject.service.utils.matcher.ProductTypeMatcher;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * A service implementation for managing Account entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDatabaseServiceImpl implements AccountDatabaseService {

    private final AccountRepository accountRepository;
    private final AccountDtoMapper accountDtoMapper;
    private final AccountCreationMapper accountCreationMapper;
    private final AccountUpdateMapper accountUpdateMapper;
    private final EntityUpdateService<Account> accountUpdateService;
    private final ProductDatabaseService productDatabaseService;
    private final AgreementDatabaseService agreementDatabaseService;
    private final AgreementInitializer agreementInitializer;
    private final AgreementDtoMapper agreementDtoMapper;
    private final ProductTypeMatcher productTypeMatcher;


    @Override
    @Transactional
    public void create(AccountDto accountDto) {
        if (accountDto == null) {
            throw new IllegalArgumentException();
        }
        Account account = accountDtoMapper.mapDtoToEntity(accountDto);
        accountRepository.save(account);
        log.info("account created");
    }

    @Override
    @Transactional
    public void create(AccountDto accountDto, String uuid) {
        if (uuid == null || accountDto == null) {
            throw new IllegalArgumentException();
        }
        UUID clientUuid = UUID.fromString(uuid);
        Account account = accountCreationMapper.mapDtoToEntity(accountDto);
        account.setClientUuid(clientUuid);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.PENDING);
        accountRepository.save(account);

        ProductType type = productTypeMatcher.matchTypes(account.getType());
        ProductStatus status = ProductStatus.ACTIVE;
        CurrencyCode currencyCode = account.getCurrencyCode();
        Product product = productDatabaseService.findProductByTypeAndStatusAndCurrencyCode(type, status, currencyCode);

        Agreement agreement = agreementInitializer.initializeAgreement(account.getUuid(), product);
        AgreementDto agreementDto = agreementDtoMapper.mapEntityToDto(agreement);
        agreementDatabaseService.create(agreementDto);
    }

    @Override
    @Transactional
    public List<AccountDto> findAllNotDeleted() {
        log.info("retrieving list of accounts");
        List<Account> accounts = accountRepository.findAllNotDeleted();
        return getDtoList(accounts);
    }

    @Override
    @Transactional
    public List<AccountDto> findDeletedAccounts() {
        log.info("retrieving list of deleted accounts");
        List<Account> accounts = accountRepository.findAllDeleted();
        return getDtoList(accounts);
    }

    @Override
    @Transactional
    public AccountDto findDtoById(String accountUuid) {
        if (accountUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(accountUuid);
        log.info("retrieving account by id {}", uuid);
        return accountDtoMapper.mapEntityToDto(
                accountRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public Account findById(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException();
        }
        log.info("retrieving account by id {}", uuid);
        return accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public List<AccountDto> findAllByStatus(String status) {
        log.info("retrieving list of accounts by status {}", status);
        List<Account> accounts = accountRepository.findAccountsByStatus(AccountStatus.valueOf(status));
        return getDtoList(accounts);
    }

    @Override
    @Transactional
    public void updateAccountDto(String existingUuid, AccountDto updatedAccountDto) {
        if (existingUuid == null || updatedAccountDto == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(existingUuid);
        Account updatedAccount = accountUpdateMapper.mapDtoToEntity(updatedAccountDto);
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        account = accountUpdateService.update(account, updatedAccount);
        accountRepository.save(account);
        log.info("updated account id {}", uuid);
    }

    @Override
    @Transactional
    public void update(UUID uuid, Account updatedAccount) {
        if (uuid == null || updatedAccount == null) {
            throw new IllegalArgumentException();
        }
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        account = accountUpdateService.update(account, updatedAccount);
        accountRepository.save(account);
        log.info("updated account id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(String accountUuid) {
        if (accountUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(accountUuid);
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        account.setDeleted(true);
        accountRepository.save(account);
        log.info("deleted account id {}", uuid);
    }

    @Override
    @Transactional
    public void blockAccountsByClientUuid(String clientUuid) {
        if (clientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        accountRepository.blockAccountsByClientUuid(uuid);
        log.info("blocked account id {}", uuid);
    }

    @Override
    @Transactional
    public List<AccountDto> findAccountsByProductIdAndStatus(String productUuid, String productStatus) {
        if (productUuid == null || productStatus == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(productUuid);
        ProductStatus status = ProductStatus.valueOf(productStatus);

        log.info("retrieving list of accounts by product id {} and product status {}", uuid, status);
        List<Account> accounts = accountRepository.findAccountsWhereProductIdAndStatusIs(uuid, status);
        return getDtoList(accounts);
    }

    @Override
    @Transactional
    public List<AccountDto> findAllDtoByClientId(String clientUuid) {
        if (clientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        log.info("retrieving list of accounts by client id {}", uuid);
        List<Account> accounts = accountRepository.findAccountsByClientUuid(uuid)
                .stream()
                .filter(account -> !account.isDeleted())
                .toList();
        return getDtoList(accounts);
    }

    @Override
    @Transactional
    public List<Account> findAllByClientId(UUID clientUuid) {
        log.info("retrieving list of accounts by client id {}", clientUuid);
        return accountRepository.findAccountsByClientUuid(clientUuid);
    }

    @Override
    @Transactional
    public Account findCurrentByClientId(UUID uuid) {
        log.info("retrieving CURRENT account by id {}", uuid);
        return accountRepository.findAccountByClientUuidAndType(uuid, AccountType.CURRENT)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public Account findSavingsByClientId(UUID uuid) {
        log.info("retrieving SAVINGS account by id {}", uuid);
        return accountRepository.findAccountByClientUuidAndType(uuid, AccountType.SAVINGS)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public List<Account> findAccountsByProductTypeAndStatus(ProductType productType, ProductStatus productStatus) {
        List<Account> accounts = accountRepository
                .findAccountsWhereProductTypeIsAndProductStatusIs(productType, productStatus);
        return Optional.of(accounts)
                .orElse(Collections.emptyList())
                .stream()
                .toList();
    }

    private List<AccountDto> getDtoList(List<Account> accounts) {
        return Optional.ofNullable(accounts)
                .orElse(Collections.emptyList())
                .stream()
                .map(accountDtoMapper::mapEntityToDto)
                .toList();
    }
}
