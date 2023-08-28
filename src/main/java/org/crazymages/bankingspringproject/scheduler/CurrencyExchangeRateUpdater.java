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


/**
 * A component responsible for updating currency exchange rates.
 * It retrieves currency rates from a currency API and stores them in the database.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeRateUpdater {

    private final CurrencyApiService currencyApiService;
    private final CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;

    /**
     * Initializes the currency exchange rate updater.
     * It should be executed after the bean is constructed.
     */
    @PostConstruct
    public void init() {
        updateCurrencyExchangeRates();
    }

    /**
     * Updates the currency exchange rates based on a scheduled cron expression.
     */
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

    /**
     * Converts a JsonNode to a HashMap of currency codes and exchange rates.
     *
     * @param jsonNode The JsonNode representing the currency rates.
     * @return The HashMap of currency codes and exchange rates.
     */
    public Map<String, BigDecimal> jsonToHashMap(JsonNode jsonNode) {
        Map<String, BigDecimal> currencyMap = new HashMap<>();

        if (jsonNode != null && jsonNode.isObject()) {
            Iterator<String> currencyCodes = jsonNode.fieldNames();

            while (currencyCodes.hasNext()) {
                String currencyCode = currencyCodes.next();
                BigDecimal exchangeRate = BigDecimal.valueOf(jsonNode.get(currencyCode).asDouble());
                currencyMap.put(currencyCode, exchangeRate);
            }
        }

        return currencyMap;
    }
}
