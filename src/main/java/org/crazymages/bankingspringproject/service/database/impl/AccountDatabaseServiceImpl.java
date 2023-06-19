package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDatabaseServiceImpl implements AccountDatabaseService {

    private final AccountRepository accountRepository;


    @Override
    public void create(Account account) {
        accountRepository.save(account);
        log.info("account created");
    }

    @Override
    public List<Account> findAll() {
        log.info("retrieving list of accounts");
        return accountRepository.findAll();
    }

    @Override
    public Account findById(UUID uuid) {
        log.info("retrieving account by id {}", uuid);
        return accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    public List<Account> findAllByStatus(String status) {
        log.info("retrieving list of accounts by status {}", status);
        return accountRepository.findAccountsByStatus(AccountStatus.valueOf(status));
    }

    @Override
    @Transactional
    public void update (UUID uuid, Account updatedAccount) {
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        if (updatedAccount.getClientUuid() != null) {
            account.setClientUuid(updatedAccount.getClientUuid());
        }
        if (updatedAccount.getName() != null) {
            account.setName(updatedAccount.getName());
        }
        if (updatedAccount.getType() != null) {
            account.setType(updatedAccount.getType());
        }
        if (updatedAccount.getStatus() != null) {
            account.setStatus(updatedAccount.getStatus());
        }
        if (updatedAccount.getBalance() != null) {
            account.setBalance(updatedAccount.getBalance());
        }
        if (updatedAccount.getCurrencyCode() != null) {
            account.setCurrencyCode(updatedAccount.getCurrencyCode());
        }
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
    public List<Account> findAccountsByProductIdAndStatus(UUID uuid, ProductStatus status) {
        log.info("retrieving list of accounts by product id {} and product status {}", uuid, status);
        return accountRepository.findAccountsWhereProductIdAndStatusIs(uuid, status);
    }

}
