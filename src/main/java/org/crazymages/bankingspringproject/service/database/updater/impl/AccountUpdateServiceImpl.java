package org.crazymages.bankingspringproject.service.database.updater.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.service.database.updater.EntityUpdateService;
import org.springframework.stereotype.Service;

@Service
public class AccountUpdateServiceImpl implements EntityUpdateService<Account> {
    @Override
    public Account update(Account account, Account accountUpdate) {
        if (account != null && accountUpdate != null) {
            if (accountUpdate.getClientUuid() != null) {
                account.setClientUuid(accountUpdate.getClientUuid());
            }
            if (accountUpdate.getName() != null) {
                account.setName(accountUpdate.getName());
            }
            if (accountUpdate.getType() != null) {
                account.setType(accountUpdate.getType());
            }
            if (accountUpdate.getStatus() != null) {
                account.setStatus(accountUpdate.getStatus());
            }
            if (accountUpdate.getBalance() != null) {
                account.setBalance(accountUpdate.getBalance());
            }
            if (accountUpdate.getCurrencyCode() != null) {
                account.setCurrencyCode(accountUpdate.getCurrencyCode());
            }
        }
        return account;
    }
}
