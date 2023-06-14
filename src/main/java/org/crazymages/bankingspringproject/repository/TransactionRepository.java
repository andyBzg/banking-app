package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /*
    findAllTransactionsWhereClientIdIs(clientId: String): List<Transaction>
    findAllTransactionsWhereAccountCurrencyIs(currency: String): List<Transaction>
    */
}
