package org.crazymages.bankingspringproject.service.api.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.service.api.CurrencyApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * A service class for interacting with a currency API to retrieve currency rates.
 */
@Service
@Slf4j
public class CurrencyApiServiceImpl implements CurrencyApiService {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${member.name}")
    private String memberName;

    public JsonNode getJsonResponseBody() {
        WebClient webClient = WebClient.create();

        return webClient.get()
                .uri(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .onErrorResume(error -> {
                    log.info(error.getMessage());
                    return Mono.just(JsonNodeFactory.instance.objectNode());
                })
                .block();
    }

    @Override
    public JsonNode getCurrencyRates() {
        JsonNode jsonNode = getJsonResponseBody();
        if (jsonNode.isNull()) {
            return JsonNodeFactory.instance.objectNode();
        }
        return jsonNode.get(memberName);
    }
}
