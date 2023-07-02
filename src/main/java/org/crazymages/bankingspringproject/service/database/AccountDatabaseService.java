package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;

import java.util.List;
import java.util.UUID;

public interface AccountDatabaseService {

    void create(Account account);

    void create(Account account, UUID uuid);

    Account findById(UUID uuid);

    List<Account> findAll();

    List<Account> findAllNotDeleted();

    List<Account> findDeletedAccounts();

    List<Account> findAllByStatus(String status);

    void update(UUID uuid, Account updatedAccount);

    void delete(UUID uuid);

    void blockAccountsByClientUuid(UUID uuid);

    List<Account> findAccountsByProductIdAndStatus(UUID uuid, ProductStatus status);

    List<Account> findAllByClientId(UUID uuid);

    Account findCurrentByClientId(UUID uuid);

    Account findSavingsByClientId(UUID uuid);
}
