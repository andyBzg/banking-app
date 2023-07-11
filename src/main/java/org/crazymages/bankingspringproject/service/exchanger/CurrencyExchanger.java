package org.crazymages.bankingspringproject.service.exchanger;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CurrencyExchanger {

    public String getJsonResponseBody() {

        WebClient webClient = WebClient.create();
        String url = "https://v6.exchangerate-api.com/v6/ebae569a80826ae25b353649/latest/USD";

        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
