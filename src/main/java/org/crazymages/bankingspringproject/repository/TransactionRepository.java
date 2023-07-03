package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * The repository interface for managing transactions.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Finds transactions by the UUID of the debit account.
     *
     * @param uuid The UUID of the debit account
     * @return The list of transactions
     */
    List<Transaction> findTransactionsByDebitAccountUuid(UUID uuid);

    /**
     * Finds transactions by the UUID of the credit account.
     *
     * @param uuid The UUID of the credit account
     * @return The list of transactions
     */
    List<Transaction> findTransactionsByCreditAccountUuid(UUID uuid);

    /**
     * Finds all transactions where the client ID matches the specified UUID.
     *
     * @param clientUuid The UUID of the client
     * @return The list of transactions
     */
    @Query("SELECT tr FROM Transaction tr " +
            "JOIN Account ac ON ac.uuid = tr.debitAccountUuid " +
            "OR ac.uuid = tr.creditAccountUuid " +
            "JOIN Client cl ON cl.uuid = ac.clientUuid " +
            "WHERE cl.uuid = :clientUuid")
    List<Transaction> findAllTransactionsWhereClientIdIs(@Param("clientUuid") UUID clientUuid);

    /**
     * Finds all transactions where the account currency matches the specified currency code.
     *
     * @param currencyCode The currency code
     * @return The list of transactions
     */
    @Query("SELECT tr FROM Transaction tr " +
            "JOIN Account ac ON ac.uuid = tr.debitAccountUuid " +
            "WHERE ac.currencyCode = :currencyCode")
    List<Transaction> findAllTransactionsWhereAccountCurrencyIs(@Param("currencyCode") CurrencyCode currencyCode);

    /**
     * Finds transactions for a specific client between the specified dates.
     *
     * @param clientUuid The UUID of the client
     * @param from       The start date
     * @param to         The end date
     * @return The list of transactions
     */
    @Query("SELECT tr FROM Transaction tr " +
            "JOIN Account ac ON ac.uuid = tr.debitAccountUuid " +
            "OR ac.uuid = tr.creditAccountUuid " +
            "JOIN Client cl ON cl.uuid = ac.clientUuid " +
            "WHERE cl.uuid = :clientUuid " +
            "AND tr.createdAt >= :from " +
            "AND tr.createdAt <= :to")
    List<Transaction> findTransactionsByClientIdBetweenDates(
            @Param("clientUuid") UUID clientUuid,
            @Param("from") Timestamp from,
            @Param("to") Timestamp to);

    /**
     * Finds transactions between the specified dates.
     *
     * @param from The start date
     * @param to   The end date
     * @return The list of transactions
     */
    @Query("SELECT tr FROM Transaction tr " +
            "WHERE tr.createdAt >= :from " +
            "AND tr.createdAt <= :to")
    List<Transaction> findTransactionsBetweenDates(@Param("from") Timestamp from, @Param("to") Timestamp to);
}
