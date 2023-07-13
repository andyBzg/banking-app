package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The repository interface for managing currency exchange rates.
 */
@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository <CurrencyExchangeRate, String> {

    /**
     * Finds all currency exchange rates that are not deleted.
     *
     * @return The list of currency exchange rates that are not deleted
     */
    @Query("SELECT cer FROM CurrencyExchangeRate cer WHERE cer.isDeleted = false ")
    List<CurrencyExchangeRate> findAllNotDeleted();
}
