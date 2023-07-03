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

/**
 * A service implementation for managing Account entities in the database.
 */
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

    /**
     * Creates a new Account entity in the database.
     *
     * @param account The Account entity to be created.
     */
    @Override
    public void create(Account account) {
        accountRepository.save(account);
        log.info("account created");
    }

    /**
     * Creates a new Account entity in the database for the specified client.
     *
     * @param account    The Account entity to be created.
     * @param clientUuid The UUID of the client associated with the account.
     */
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

    /**
     * Composes an Agreement entity based on the specified account UUID and product.
     *
     * @param accountUuid The UUID of the account.
     * @param product     The Product entity.
     * @return The composed Agreement entity.
     */
    private Agreement composeAgreement(UUID accountUuid, Product product) {
        Agreement agreement = new Agreement();
        agreement.setAccountUuid(accountUuid);
        agreement.setProductUuid(product.getUuid());
        agreement.setInterestRate(product.getInterestRate());
        agreement.setStatus(AgreementStatus.PENDING);
        agreement.setAmount(BigDecimal.valueOf(0));
        return agreement;
    }

    /**
     * Retrieves a list of all Account entities from the database.
     *
     * @return A list of all Account entities.
     */
    @Override
    public List<Account> findAll() {
        log.info("retrieving list of accounts");
        List<Account> accounts = accountRepository.findAll();
        return listValidator.validate(accounts);
    }

    /**
     * Retrieves a list of all non-deleted Account entities from the database.
     *
     * @return A list of non-deleted Account entities.
     */
    @Override
    @Transactional
    public List<Account> findAllNotDeleted() {
        log.info("retrieving list of accounts");
        List<Account> accounts = accountRepository.findAllNotDeleted();
        return listValidator.validate(accounts);
    }

    /**
     * Retrieves a list of all deleted Account entities from the database.
     *
     * @return A list of deleted Account entities.
     */
    @Override
    @Transactional
    public List<Account> findDeletedAccounts() {
        log.info("retrieving list of deleted accounts");
        List<Account> deletedAccounts = accountRepository.findAllDeleted();
        return listValidator.validate(deletedAccounts);
    }

    /**
     * Retrieves an Account entity from the database by its UUID.
     *
     * @param uuid The UUID of the Account to retrieve.
     * @return The Account entity with the specified UUID.
     * @throws DataNotFoundException if no Account entity is found with the specified UUID.
     */
    @Override
    public Account findById(UUID uuid) {
        log.info("retrieving account by id {}", uuid);
        return accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    /**
     * Retrieves a list of Account entities from the database by their status.
     *
     * @param status The status of the Account entities to retrieve.
     * @return A list of Account entities with the specified status.
     */
    @Override
    @Transactional
    public List<Account> findAllByStatus(String status) {
        log.info("retrieving list of accounts by status {}", status);
        List<Account> accounts = accountRepository.findAccountsByStatus(AccountStatus.valueOf(status));
        return listValidator.validate(accounts);
    }

    /**
     * Updates an existing Account entity in the database.
     *
     * @param uuid           The UUID of the Account to update.
     * @param updatedAccount The updated Account entity.
     * @throws DataNotFoundException if no Account entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void update(UUID uuid, Account updatedAccount) {
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        account = accountUpdateService.update(account, updatedAccount);
        accountRepository.save(account);
        log.info("updated account id {}", uuid);
    }

    /**
     * Deletes an existing Account entity from the database.
     *
     * @param uuid The UUID of the Account to delete.
     * @throws DataNotFoundException if no Account entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void delete(UUID uuid) {
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        account.setDeleted(true);
        accountRepository.save(account);
        log.info("deleted account id {}", uuid);
    }

    /**
     * Blocks all accounts associated with the specified client UUID.
     *
     * @param uuid The UUID of the client.
     */
    @Override
    @Transactional
    public void blockAccountsByClientUuid(UUID uuid) {
        accountRepository.blockAccountsByClientUuid(uuid);
        log.info("blocked account id {}", uuid);
    }

    /**
     * Retrieves a list of Account entities associated with the specified product UUID and status.
     *
     * @param uuid   The UUID of the product.
     * @param status The status of the Account entities to retrieve.
     * @return A list of Account entities with the specified product UUID and status.
     */
    @Override
    @Transactional
    public List<Account> findAccountsByProductIdAndStatus(UUID uuid, ProductStatus status) {
        log.info("retrieving list of accounts by product id {} and product status {}", uuid, status);
        List<Account> accounts = accountRepository.findAccountsWhereProductIdAndStatusIs(uuid, status);
        return listValidator.validate(accounts);
    }

    /**
     * Retrieves a list of Account entities associated with the specified client UUID.
     *
     * @param uuid The UUID of the client.
     * @return A list of Account entities associated with the specified client UUID.
     */
    @Override
    @Transactional
    public List<Account> findAllByClientId(UUID uuid) {
        log.info("retrieving list of accounts by client id {}", uuid);
        List<Account> accounts = accountRepository.findAccountsByClientUuid(uuid);
        return listValidator.validate(accounts);
    }

    /**
     * Retrieves the current Account entity associated with the specified client UUID.
     *
     * @param uuid The UUID of the client.
     * @return The current Account entity associated with the specified client UUID.
     * @throws DataNotFoundException if no current Account entity is found for the client.
     */
    @Override
    @Transactional
    public Account findCurrentByClientId(UUID uuid) {
        log.info("retrieving CURRENT account by id {}", uuid);
        return accountRepository.findAccountByClientUuidAndType(uuid, AccountType.CURRENT)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    /**
     * Retrieves the savings Account entity associated with the specified client UUID.
     *
     * @param uuid The UUID of the client.
     * @return The savings Account entity associated with the specified client UUID.
     * @throws DataNotFoundException if no savings Account entity is found for the client.
     */
    @Override
    @Transactional
    public Account findSavingsByClientId(UUID uuid) {
        log.info("retrieving SAVINGS account by id {}", uuid);
        return accountRepository.findAccountByClientUuidAndType(uuid, AccountType.SAVINGS)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }
}
