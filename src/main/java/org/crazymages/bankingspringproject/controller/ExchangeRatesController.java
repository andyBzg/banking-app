package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDTO;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ExchangeRatesController {

    private final CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;

    @GetMapping(value = "/get-exchange-rates")
    public ResponseEntity<List<CurrencyExchangeRateDTO>> getExchangeRates() {
        List<CurrencyExchangeRateDTO> exchangeRates = currencyExchangeRateDatabaseService.findAllDTOs();
        return ResponseEntity.ok(exchangeRates);
    }
}
