package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The repository interface for managing clients.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    /**
     * Finds clients by status.
     *
     * @param status The client status
     * @return The list of clients matching the status
     */
    List<Client> findClientsByStatusIs(ClientStatus status);

    /**
     * Finds all clients where balance is greater than the specified amount.
     *
     * @param balance The minimum balance
     * @return The list of clients with balance greater than the specified amount
     */
    @Query("SELECT cl FROM Client cl " +
            "JOIN Account ac ON ac.clientUuid = cl.uuid " +
            "WHERE ac.balance > :balance")
    List<Client> findAllClientsWhereBalanceMoreThan(@Param("balance") BigDecimal balance);

    /**
     * Finds all clients where the number of transactions is greater than the specified count.
     *
     * @param count The minimum number of transactions
     * @return The list of clients with more than the specified number of transactions
     */
    @Query("SELECT cl FROM Client cl " +
            "JOIN Account ac ON ac.clientUuid = cl.uuid " +
            "JOIN Transaction tr ON tr.debitAccountUuid = ac.uuid " +
            "GROUP BY cl " +
            "HAVING COUNT(tr) > :count")
    List<Client> findAllClientsWhereTransactionMoreThan(@Param("count") Integer count);

    /**
     * Calculates the total balance of a client based on their UUID.
     *
     * @param uuid The client UUID
     * @return The total balance of the client
     */
    @Query("SELECT SUM(ac.balance) FROM Account ac " +
            "JOIN Client cl ON cl.uuid = ac.clientUuid " +
            "WHERE cl.uuid = :uuid")
    BigDecimal calculateTotalBalanceByClientUuid(@Param("uuid") UUID uuid);

    /**
     * Checks if the client status is blocked.
     *
     * @param uuid The client UUID
     * @return {@code true} if the client status is blocked, {@code false} otherwise
     */
    @Query("SELECT CASE WHEN cl.status = 'BLOCKED' THEN TRUE ELSE FALSE END " +
            "FROM Client cl " +
            "WHERE cl.uuid = :uuid")
    Boolean isClientStatusBlocked(@Param("uuid") UUID uuid);

    /**
     * Finds all active clients with two different account types.
     *
     * @param firstType  The first account type
     * @param secondType The second account type
     * @return The list of active clients with two different account types
     */
    @Query("SELECT DISTINCT cl FROM Client cl " +
            "JOIN Account ac ON ac.clientUuid = cl.uuid " +
            "WHERE ac.type = :firstType OR ac.type = :secondType " +
            "AND cl.status = 'ACTIVE'")
    List<Client> findAllActiveClientsWithTwoDifferentAccountTypes(
            @Param("firstType") AccountType firstType,
            @Param("secondType") AccountType secondType);

    /**
     * Finds all clients that are not deleted.
     *
     * @return The list of clients that are not deleted
     */
    @Query("SELECT cl FROM Client cl WHERE cl.isDeleted = false")
    List<Client> findAllNotDeleted();

    /**
     * Finds all deleted clients.
     *
     * @return The list of deleted clients
     */
    @Query("SELECT cl FROM Client cl WHERE cl.isDeleted = true")
    List<Client> findAllDeleted();

    /**
     * Blocks client by its UUID.
     *
     * @param uuid The client UUID
     */
    @Query("UPDATE Client cl SET cl.status = 'BLOCKED' WHERE cl.uuid = :uuid")
    void blockClientById(@Param("uuid") UUID uuid);
}
