package com.bank.pos.client;

import com.bank.pos.config.MpcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MpcClientConfig {

    @Bean
    public WebClient mpcWebClient(MpcProperties props) {
        WebClient.Builder b = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        if (props.getBearerToken() != null && !props.getBearerToken().isBlank()) {
            b.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getBearerToken().trim());
        }

        return b.filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            // keep default behavior; placeholder for future logging
            return reactor.core.publisher.Mono.just(clientResponse);
        })).build();
    }
}

