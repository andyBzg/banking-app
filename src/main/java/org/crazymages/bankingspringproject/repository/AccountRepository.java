package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findAccountsByStatus(AccountStatus status);

    //TODO findAccountsWhereProductIdIsAndStatusIs(productId: String, status: String): List<Account>

}
