package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findAccountsByStatus(AccountStatus status);

//    List<Account> findAccountsByUuid(UUID uuid);

//    @Modifying
//    @Query("UPDATE Account a SET a.status = :newStatus WHERE a.clientUuid = :clientUuid")
//    void updateAccountStatusByUuid(@Param("clientUuid") UUID clientUuid, @Param("newStatus") AccountStatus newStatus);

    @Modifying
    @Query("UPDATE Account ac SET ac.status = 'BLOCKED' WHERE ac.clientUuid = :clientUuid")
    void blockAccountsByClientUuid(@Param("clientUuid") UUID clientUuid);

    @Query("SELECT ac FROM Account ac " +
            "JOIN Agreement ag ON ag.accountUuid = ac.uuid " +
            "JOIN Product pr ON pr.uuid = ag.productUuid " +
            "WHERE pr.uuid = :productUuid " +
            "AND pr.status = :status")
    List<Account> findAccountsWhereProductIdAndStatusIs(
            @Param("productUuid") UUID productUuid,
            @Param("status") ProductStatus status);
}
