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

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findTransactionsByDebitAccountUuid(UUID uuid);

    List<Transaction> findTransactionsByCreditAccountUuid(UUID uuid);

    @Query("SELECT tr FROM Transaction tr " +
            "JOIN Account ac ON ac.uuid = tr.debitAccountUuid " +
            "OR ac.uuid = tr.creditAccountUuid " +
            "JOIN Client cl ON cl.uuid = ac.clientUuid " +
            "WHERE cl.uuid = :clientUuid")
    List<Transaction> findAllTransactionsWhereClientIdIs(@Param("clientUuid") UUID clientUuid);

    @Query("SELECT tr FROM Transaction tr " +
            "JOIN Account ac ON ac.uuid = tr.debitAccountUuid " +
            "WHERE ac.currencyCode = :currencyCode")
    List<Transaction> findAllTransactionsWhereAccountCurrencyIs(@Param("currencyCode")CurrencyCode currencyCode);

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

    @Query("SELECT tr FROM Transaction tr " +
            "WHERE tr.createdAt >= :from " +
            "AND tr.createdAt <= :to")
    List<Transaction> findTransactionsBetweenDates(@Param("from") Timestamp from, @Param("to") Timestamp to);
}
