package org.crazymages.bankingspringproject.service.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyApiService {

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
                .block();
    }

    public JsonNode getCurrencyRates() {
        JsonNode jsonNode = getJsonResponseBody();
        return jsonNode.get(memberName);
    }
}
