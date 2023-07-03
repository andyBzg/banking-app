package org.crazymages.bankingspringproject.service.utils.updater.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Component;

/**
 * A class implementing the EntityUpdateService interface for updating Account entities.
 * It provides custom update logic for the Account entity.
 */
@Component
public class AccountUpdateServiceImpl implements EntityUpdateService<Account> {

    /**
     * Updates the given Account entity with the provided Account update.
     * Only non-null fields in the Account update will be applied to the original Account.
     *
     * @param account The existing entity to be updated.
     * @param accountUpdate The entity containing the updated data.
     * @return The updated Account entity.
     */
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
