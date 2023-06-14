package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AccountRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountDatabaseServiceImpl implements AccountDatabaseService {

    private final AccountRepository accountRepository;


    @Override
    public void create(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(UUID uuid) {
        Optional<Account> accountOptional = accountRepository.findById(uuid);
        return accountOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    public List<Account> findAllByStatus(String status) {
        return accountRepository.findAccountsByStatus(AccountStatus.valueOf(status));
    }

    @Override
    @Transactional
    public Account update(UUID uuid, Account accountUpdate) {
        Optional<Account> accountOptional = accountRepository.findById(uuid);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setClient(accountUpdate.getClient());
            account.setName(accountUpdate.getName());
            account.setType(accountUpdate.getType());
            account.setStatus(accountUpdate.getStatus());
            account.setBalance(accountUpdate.getBalance());
            account.setCurrencyCode(accountUpdate.getCurrencyCode());
            accountRepository.save(account);
        }
        return accountOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    public void delete(UUID uuid) {
        accountRepository.deleteById(uuid);
    }

}
