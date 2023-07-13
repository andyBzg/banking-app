package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDTO;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;

import java.util.List;

/**
 * A service interface for managing currency exchange rates in the database.
 * It provides methods for creating, retrieving, updating, and deleting currency exchange rates.
 */
public interface CurrencyExchangeRateDatabaseService {

    /**
     * Creates a new currency exchange rate in the database.
     *
     * @param currencyExchangeRate The currency exchange rate to be created.
     */
    void create(CurrencyExchangeRate currencyExchangeRate);

    /**
     * Retrieves all currency exchange rates from the database.
     *
     * @return A list of all currency exchange rates.
     */
    List<CurrencyExchangeRate> findAll();

    /**
     * Retrieves all currency exchange rate DTOs from the database.
     *
     * @return A list of all currency exchange rate DTOs.
     */
    List<CurrencyExchangeRateDTO> findAllDTOs();

    /**
     * Retrieves a currency exchange rate from the database by its currency code.
     *
     * @param currencyCode The currency code of the currency exchange rate to retrieve.
     * @return The currency exchange rate with the specified currency code, or null if not found.
     */
    CurrencyExchangeRate findById(String currencyCode);

    /**
     * Updates a currency exchange rate in the database with the specified currency code.
     *
     * @param currencyCode         The currency code of the currency exchange rate to update.
     * @param currencyExchangeRate The updated currency exchange rate.
     */
    void update(String currencyCode, CurrencyExchangeRate currencyExchangeRate);

    /**
     * Deletes a currency exchange rate from the database with the specified currency code.
     *
     * @param currencyCode The currency code of the currency exchange rate to delete.
     */
    void delete(String currencyCode);
}
