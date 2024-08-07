package com.example.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${api.github.url}")
    private String githubApiUrl;

    @Bean
    public WebClient webClient(ObjectMapper objectMapper) {
        var exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonEncoder(
                            new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    configurer.defaultCodecs().jackson2JsonDecoder(
                            new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                }).build();

        return WebClient.builder()
                .baseUrl(githubApiUrl)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }
}
