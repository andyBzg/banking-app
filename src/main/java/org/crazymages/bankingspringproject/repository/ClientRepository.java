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

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    List<Client> findClientsByStatusIs(ClientStatus status);

    @Query("SELECT cl FROM Client cl " +
            "JOIN Account ac ON ac.clientUuid = cl.uuid " +
            "WHERE ac.balance > :balance")
    List<Client> findAllClientsWhereBalanceMoreThan(@Param("balance") BigDecimal balance);

    @Query("SELECT cl FROM Client cl " +
            "JOIN Account ac ON ac.clientUuid = cl.uuid " +
            "JOIN Transaction tr ON tr.debitAccountUuid = ac.uuid " +
            "GROUP BY cl " +
            "HAVING COUNT(tr) > :count")
    List<Client> findAllClientsWhereTransactionMoreThan(@Param("count") Integer count);

    @Query("SELECT SUM(a.balance) FROM Account a " +
            "JOIN Client c ON c.uuid = a.clientUuid " +
            "WHERE c.uuid = :uuid")
    BigDecimal calculateTotalBalanceByClientUuid(@Param("uuid") UUID uuid);

    @Query("SELECT CASE WHEN cl.status = 'BLOCKED' THEN TRUE ELSE FALSE END FROM Client cl WHERE cl.uuid = :uuid")
    Boolean isClientStatusBlocked(@Param("uuid") UUID uuid);

    @Query("SELECT DISTINCT cl FROM Client cl " +
            "JOIN Account ac ON ac.clientUuid = cl.uuid " +
            "WHERE ac.type = :firstType OR ac.type = :secondType " +
            "AND cl.status = 'ACTIVE'")
    List<Client> findAllActiveClientsWithTwoDifferentAccountTypes(
            @Param("firstType") AccountType firstType,
            @Param("secondType") AccountType secondType);
}
