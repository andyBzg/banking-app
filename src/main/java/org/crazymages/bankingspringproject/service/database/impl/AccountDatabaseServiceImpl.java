package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.dto.AgreementDto;
import org.crazymages.bankingspringproject.entity.*;
import org.crazymages.bankingspringproject.entity.enums.*;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.crazymages.bankingspringproject.service.utils.creator.AgreementInitializer;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.AccountDtoMapper;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.AgreementDtoMapper;
import org.crazymages.bankingspringproject.service.utils.matcher.ProductTypeMatcher;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EntityUpdateService<Account> accountUpdateService;
    private final ProductDatabaseService productDatabaseService;
    private final AgreementDatabaseService agreementDatabaseService;
    private final AgreementInitializer agreementInitializer;
    private final AgreementDtoMapper agreementDtoMapper;
    private final ProductTypeMatcher productTypeMatcher;


    @Override
    @Transactional
    public void create(AccountDto accountDto) {
        Account account = accountDtoMapper.mapDtoToEntity(accountDto);
        accountRepository.save(account);
        log.info("account created");
    }

    @Override
    public void create(AccountDto accountDto, UUID clientUuid) {
        Account account = accountDtoMapper.mapDtoToEntity(accountDto);
        account.setClientUuid(clientUuid);
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
        return accountDtoMapper.getDtoList(accounts);
    }

    @Override
    @Transactional
    public List<AccountDto> findDeletedAccounts() {
        log.info("retrieving list of deleted accounts");
        List<Account> accounts = accountRepository.findAllDeleted();
        return accountDtoMapper.getDtoList(accounts);
    }

    @Override
    @Transactional
    public AccountDto findById(UUID uuid) {
        log.info("retrieving account by id {}", uuid);
        return accountDtoMapper.mapEntityToDto(
                accountRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public List<AccountDto> findAllByStatus(String status) {
        log.info("retrieving list of accounts by status {}", status);
        List<Account> accounts = accountRepository.findAccountsByStatus(AccountStatus.valueOf(status));
        return accountDtoMapper.getDtoList(accounts);
    }

    @Override
    @Transactional
    public void update(UUID uuid, AccountDto updatedAccountDto) {
        Account updatedAccount = accountDtoMapper.mapDtoToEntity(updatedAccountDto);
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        account = accountUpdateService.update(account, updatedAccount);
        accountRepository.save(account);
        log.info("updated account id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        account.setDeleted(true);
        accountRepository.save(account);
        log.info("deleted account id {}", uuid);
    }

    @Override
    @Transactional
    public void blockAccountsByClientUuid(UUID uuid) {
        accountRepository.blockAccountsByClientUuid(uuid);
        log.info("blocked account id {}", uuid);
    }

    @Override
    @Transactional
    public List<AccountDto> findAccountsByProductIdAndStatus(UUID uuid, ProductStatus status) {
        log.info("retrieving list of accounts by product id {} and product status {}", uuid, status);
        List<Account> accounts = accountRepository.findAccountsWhereProductIdAndStatusIs(uuid, status);
        return accountDtoMapper.getDtoList(accounts);
    }

    @Override
    @Transactional
    public List<AccountDto> findAllDtoByClientId(UUID uuid) {
        log.info("retrieving list of accounts by client id {}", uuid);
        List<Account> accounts = accountRepository.findAccountsByClientUuid(uuid);
        return accountDtoMapper.getDtoList(accounts);
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
}
