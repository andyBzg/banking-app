package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Account;

import java.util.List;
import java.util.UUID;

public interface AccountDatabaseService {

    void create(Account account);

    Account findById(UUID uuid);

    List<Account> findAll();

    List<Account> findAllByStatus(String status);

    Account update(UUID uuid, Account account);

    void delete(UUID uuid);

}
