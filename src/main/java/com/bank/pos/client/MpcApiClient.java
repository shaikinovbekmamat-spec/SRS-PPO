package com.bank.pos.client;

import com.bank.pos.config.MpcProperties;
import com.bank.pos.dto.MpcTransactionDto;
import com.bank.pos.dto.MpcTransactionsPageDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MpcApiClient {
    private final WebClient webClient;
    private final MpcProperties props;

    public MpcApiClient(WebClient mpcWebClient, MpcProperties props) {
        this.webClient = mpcWebClient;
        this.props = props;
    }

    public List<MpcTransactionDto> fetchTransactions(LocalDate date) {
        int page = 0;
        int size = props.getPolling().getPageSize();
        List<MpcTransactionDto> result = new ArrayList<>();

        while (true) {
            final int currentPage = page;
            MpcTransactionsPageDto dto = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/transactions")
                            .queryParam("date", date)
                            .queryParam("bankCode", props.getBankCode())
                            .queryParam("page", currentPage)
                            .queryParam("size", size)
                            .build())
                    .retrieve()
                    .bodyToMono(MpcTransactionsPageDto.class)
                    .timeout(Duration.ofSeconds(25))
                    .block();

            if (dto == null) {
                return result;
            }

            result.addAll(dto.getContent());
            page++;
            if (page >= Math.max(dto.getTotalPages(), 1)) {
                return result;
            }
        }
    }
}

