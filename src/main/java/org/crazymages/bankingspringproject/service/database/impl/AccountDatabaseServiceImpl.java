package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AccountDTO;
import org.crazymages.bankingspringproject.dto.AgreementDTO;
import org.crazymages.bankingspringproject.entity.*;
import org.crazymages.bankingspringproject.entity.enums.*;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.crazymages.bankingspringproject.service.utils.creator.AgreementCreator;
import org.crazymages.bankingspringproject.service.utils.mapper.AccountDTOMapper;
import org.crazymages.bankingspringproject.service.utils.mapper.AgreementDTOMapper;
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
    private final AccountDTOMapper accountDTOMapper;
    private final EntityUpdateService<Account> accountUpdateService;
    private final ProductDatabaseService productDatabaseService;
    private final AgreementDatabaseService agreementDatabaseService;
    private final AgreementCreator agreementCreator;
    private final AgreementDTOMapper agreementDTOMapper;
    private final ProductTypeMatcher productTypeMatcher;


    @Override
    @Transactional
    public void create(AccountDTO accountDTO) {
        Account account = accountDTOMapper.mapToAccount(accountDTO);
        accountRepository.save(account);
        log.info("account created");
    }

    @Override
    @Transactional
    public void create(AccountDTO accountDTO, UUID clientUuid) {
        CurrencyCode currencyCode = accountDTO.getCurrencyCode();
        ProductStatus status = ProductStatus.ACTIVE;
        ProductType type = productTypeMatcher.matchTypes(accountDTO.getType());
        Product product = productDatabaseService.findProductByTypeAndStatusAndCurrencyCode(type, status, currencyCode);

        Account account = accountDTOMapper.mapToAccount(accountDTO);
        account.setClientUuid(clientUuid);
        account.setStatus(AccountStatus.PENDING);
        accountRepository.save(account);

        UUID accountUuid = account.getUuid();
        Agreement agreement = agreementCreator.apply(accountUuid, product);
        AgreementDTO agreementDTO = agreementDTOMapper.mapToAgreementDTO(agreement);
        agreementDatabaseService.create(agreementDTO);
    }

    @Override
    @Transactional
    public List<AccountDTO> findAllNotDeleted() {
        log.info("retrieving list of accounts");
        List<Account> accounts = accountRepository.findAllNotDeleted();
        return accountDTOMapper.getListOfAccountDTOs(accounts);
    }

    @Override
    @Transactional
    public List<AccountDTO> findDeletedAccounts() {
        log.info("retrieving list of deleted accounts");
        List<Account> accounts = accountRepository.findAllDeleted();
        return accountDTOMapper.getListOfAccountDTOs(accounts);
    }

    @Override
    @Transactional
    public AccountDTO findById(UUID uuid) {
        log.info("retrieving account by id {}", uuid);
        return accountDTOMapper.mapToAccountDTO(
                accountRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public List<AccountDTO> findAllByStatus(String status) {
        log.info("retrieving list of accounts by status {}", status);
        List<Account> accounts = accountRepository.findAccountsByStatus(AccountStatus.valueOf(status));
        return accountDTOMapper.getListOfAccountDTOs(accounts);
    }

    @Override
    @Transactional
    public void update(UUID uuid, AccountDTO updatedAccountDTO) {
        Account updatedAccount = accountDTOMapper.mapToAccount(updatedAccountDTO);
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
    public List<AccountDTO> findAccountsByProductIdAndStatus(UUID uuid, ProductStatus status) {
        log.info("retrieving list of accounts by product id {} and product status {}", uuid, status);
        List<Account> accounts = accountRepository.findAccountsWhereProductIdAndStatusIs(uuid, status);
        return accountDTOMapper.getListOfAccountDTOs(accounts);
    }

    @Override
    @Transactional
    public List<AccountDTO> findAllByClientId(UUID uuid) {
        log.info("retrieving list of accounts by client id {}", uuid);
        List<Account> accounts = accountRepository.findAccountsByClientUuid(uuid);
        return accountDTOMapper.getListOfAccountDTOs(accounts);
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
