package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;

import java.util.List;
import java.util.UUID;

public interface AccountDatabaseService {

    void create(Account account);

    Account findById(UUID uuid);

    List<Account> findAll();

    List<Account> findAllByStatus(String status);

    void update(UUID uuid, Account updatedAccount);

    void delete(UUID uuid);

    void blockAccountsByClientUuid(UUID uuid);

    List<Account> findAccountsByProductIdAndStatus(UUID uuid, ProductStatus status);

//    void updateAccountStatusByUuid(UUID uuid, AccountStatus status);
}
