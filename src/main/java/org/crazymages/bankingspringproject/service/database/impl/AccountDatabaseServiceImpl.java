package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.*;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.crazymages.bankingspringproject.service.utils.converter.EnumTypeMatcher;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.crazymages.bankingspringproject.service.utils.validator.ListValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDatabaseServiceImpl implements AccountDatabaseService {

    private final AccountRepository accountRepository;
    private final EntityUpdateService<Account> accountUpdateService;
    private final ProductDatabaseService productDatabaseService;
    private final AgreementDatabaseService agreementDatabaseService;
    private final ListValidator<Account> listValidator;
    private final EnumTypeMatcher enumTypeMatcher;


    @Override
    public void create(Account account) {
        accountRepository.save(account);
        log.info("account created");
    }

    @Override
    @Transactional
    public void create(Account account, UUID clientUuid) {
        CurrencyCode currencyCode = account.getCurrencyCode();
        ProductStatus status = ProductStatus.ACTIVE;
        ProductType type = enumTypeMatcher.matchTypes(account.getType());
        Product product = productDatabaseService.findProductByTypeAndStatusAndCurrencyCode(type, status, currencyCode);

        account.setClientUuid(clientUuid);
        account.setStatus(AccountStatus.PENDING);
        accountRepository.save(account);

        UUID accountUuid = account.getUuid();
        Agreement agreement = composeAgreement(accountUuid, product);
        agreementDatabaseService.create(agreement);
    }

    private Agreement composeAgreement(UUID accountUuid, Product product) {
        Agreement agreement = new Agreement();
        agreement.setAccountUuid(accountUuid);
        agreement.setProductUuid(product.getUuid());
        agreement.setInterestRate(product.getInterestRate());
        agreement.setStatus(AgreementStatus.PENDING);
        agreement.setAmount(BigDecimal.valueOf(0));
        return agreement;
    }

    @Override
    public List<Account> findAll() {
        log.info("retrieving list of accounts");
        List<Account> accounts = accountRepository.findAll();
        return listValidator.validate(accounts);
    }

    @Override
    @Transactional
    public List<Account> findAllNotDeleted() {
        log.info("retrieving list of accounts");
        List<Account> accounts = accountRepository.findAllNotDeleted();
        return listValidator.validate(accounts);
    }

    @Override
    @Transactional
    public List<Account> findDeletedAccounts() {
        log.info("retrieving list of deleted accounts");
        List<Account> deletedAccounts = accountRepository.findAllDeleted();
        return listValidator.validate(deletedAccounts);
    }

    @Override
    public Account findById(UUID uuid) {
        log.info("retrieving account by id {}", uuid);
        return accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public List<Account> findAllByStatus(String status) {
        log.info("retrieving list of accounts by status {}", status);
        List<Account> accounts = accountRepository.findAccountsByStatus(AccountStatus.valueOf(status));
        return listValidator.validate(accounts);
    }

    @Override
    @Transactional
    public void update (UUID uuid, Account updatedAccount) {
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
    public List<Account> findAccountsByProductIdAndStatus(UUID uuid, ProductStatus status) {
        log.info("retrieving list of accounts by product id {} and product status {}", uuid, status);
        List<Account> accounts = accountRepository.findAccountsWhereProductIdAndStatusIs(uuid, status);
        return listValidator.validate(accounts);
    }

    @Override
    @Transactional
    public List<Account> findAllByClientId(UUID uuid) {
        log.info("retrieving list of accounts by client id {}", uuid);
        List<Account> accounts = accountRepository.findAccountsByClientUuid(uuid);
        return listValidator.validate(accounts);
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
}
