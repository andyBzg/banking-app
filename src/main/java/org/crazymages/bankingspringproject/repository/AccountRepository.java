package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The repository interface for managing accounts.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    /**
     * Finds accounts by status.
     *
     * @param status The account status
     * @return The list of accounts matching the given status
     */
    List<Account> findAccountsByStatus(AccountStatus status);

    /**
     * Blocks accounts by client UUID.
     *
     * @param clientUuid The client UUID
     */
    @Modifying
    @Query("UPDATE Account ac SET ac.status = 'BLOCKED' WHERE ac.clientUuid = :clientUuid")
    void blockAccountsByClientUuid(@Param("clientUuid") UUID clientUuid);

    /**
     * Finds accounts where product ID and status match.
     *
     * @param productUuid The product UUID
     * @param status      The product status
     * @return The list of accounts matching the given product ID and status
     */
    @Query("SELECT ac FROM Account ac " +
            "JOIN Agreement ag ON ag.accountUuid = ac.uuid " +
            "JOIN Product pr ON pr.uuid = ag.productUuid " +
            "WHERE pr.uuid = :productUuid " +
            "AND pr.status = :status")
    List<Account> findAccountsWhereProductIdAndStatusIs(
            @Param("productUuid") UUID productUuid,
            @Param("status") ProductStatus status);

    /**
     * Finds accounts by client UUID.
     *
     * @param uuid The client UUID
     * @return The list of accounts belonging to the client
     */
    List<Account> findAccountsByClientUuid(UUID uuid);

    /**
     * Finds an account by client UUID and account type.
     *
     * @param uuid The client UUID
     * @param type The account type
     * @return The optional account matching the given client UUID and account type
     */
    Optional<Account> findAccountByClientUuidAndType(UUID uuid, AccountType type);

    /**
     * Finds all accounts that are not deleted.
     *
     * @return The list of accounts that are not deleted
     */
    @Query("SELECT ac FROM Account ac WHERE ac.isDeleted = false")
    List<Account> findAllNotDeleted();

    /**
     * Finds all deleted accounts.
     *
     * @return The list of deleted accounts
     */
    @Query("SELECT ac FROM Account ac WHERE ac.isDeleted = true")
    List<Account> findAllDeleted();
}
