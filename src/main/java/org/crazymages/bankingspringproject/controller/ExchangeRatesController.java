package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDto;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A controller class for handling exchange rates related endpoints.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ExchangeRatesController {

    private final CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;

    /**
     * Retrieves the exchange rates from the database.
     *
     * @return The ResponseEntity containing the list of exchange rates.
     */
    @GetMapping(value = "/exchange/get-rates")
    public ResponseEntity<List<CurrencyExchangeRateDto>> getExchangeRates() {
        List<CurrencyExchangeRateDto> exchangeRates = currencyExchangeRateDatabaseService.findAllRates();
        log.info("endpoint request: get exchange rates");
        return exchangeRates.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(exchangeRates);
    }
}
