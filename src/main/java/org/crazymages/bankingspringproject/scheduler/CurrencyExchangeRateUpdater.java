package org.crazymages.bankingspringproject.scheduler;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.crazymages.bankingspringproject.service.api.CurrencyApiService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeRateUpdater {

    private final CurrencyApiService currencyApiService;
    private final CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;

    @PostConstruct
    public void init() {
        updateCurrencyExchangeRates();
    }

    @Scheduled(cron = "${currency.rates.check}")
    public void updateCurrencyExchangeRates() {
        JsonNode jsonNode = currencyApiService.getCurrencyRates();
        Map<String, BigDecimal> currencyMap = jsonToHashMap(jsonNode);

        currencyMap.forEach((currencyCode, exchangeRate) -> {
            CurrencyExchangeRate currencyRate = new CurrencyExchangeRate();
            currencyRate.setCurrencyCode(currencyCode);
            currencyRate.setExchangeRate(exchangeRate);
            currencyExchangeRateDatabaseService.create(currencyRate);
        });

    }

    public Map<String, BigDecimal> jsonToHashMap(JsonNode jsonNode) {
        Map<String, BigDecimal> currencyMap = new HashMap<>();
        Iterator<String> currencyCodes = jsonNode.fieldNames();

        while (currencyCodes.hasNext()) {
            String currencyCode = currencyCodes.next();
            BigDecimal exchangeRate = BigDecimal.valueOf(jsonNode.get(currencyCode).asDouble());
            currencyMap.put(currencyCode, exchangeRate);
        }

        return currencyMap;
    }
}
