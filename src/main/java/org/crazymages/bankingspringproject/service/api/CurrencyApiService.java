package org.crazymages.bankingspringproject.service.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * A service class for interacting with a currency API to retrieve currency rates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyApiService {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${member.name}")
    private String memberName;

    /**
     * Retrieves the JSON response body from the currency API.
     *
     * @return The JSON response body as a JsonNode.
     */
    public JsonNode getJsonResponseBody() {
        WebClient webClient = WebClient.create();

        return webClient.get()
                .uri(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    /**
     * Retrieves the currency rates from the JSON response body.
     *
     * @return The currency rates as a JsonNode.
     */
    public JsonNode getCurrencyRates() {
        JsonNode jsonNode = getJsonResponseBody();
        return jsonNode.get(memberName);
    }
}
