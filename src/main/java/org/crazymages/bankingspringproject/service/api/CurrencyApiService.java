package org.crazymages.bankingspringproject.service.api;

import com.fasterxml.jackson.databind.JsonNode;

public interface CurrencyApiService {
    /**
     * Retrieves the JSON response body from the currency API.
     *
     * @return The JSON response body as a JsonNode.
     */
    JsonNode getCurrencyRates();
}
