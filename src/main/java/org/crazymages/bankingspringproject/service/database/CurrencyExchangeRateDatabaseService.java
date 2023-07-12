package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDTO;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;

import java.util.List;


public interface CurrencyExchangeRateDatabaseService {

    void create(CurrencyExchangeRate currencyExchangeRate);

    List<CurrencyExchangeRate> findAll();

    List<CurrencyExchangeRateDTO> findAllDTOs();

    CurrencyExchangeRate findById(String currencyCode);

    void update(String currencyCode, CurrencyExchangeRate currencyExchangeRate);

    void delete(String currencyCode);
}
