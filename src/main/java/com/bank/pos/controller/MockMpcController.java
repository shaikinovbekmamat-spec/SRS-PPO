package com.bank.pos.controller;

import com.bank.pos.dto.MpcTransactionDto;
import com.bank.pos.dto.MpcTransactionsPageDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MockMpcController {

    @GetMapping("/v1/transactions")
    public MpcTransactionsPageDto getMockTransactions(
            @RequestParam("date") String dateStr,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        MpcTransactionsPageDto response = new MpcTransactionsPageDto();
        
        // Только на первой странице возвращаем данные для демонстрации
        if (page == 0) {
            LocalDate date = LocalDate.parse(dateStr);
            List<MpcTransactionDto> txs = new ArrayList<>();

            // Создаем 20 транзакций для наглядности
            for (int i = 1; i <= 20; i++) {
                String curr = (i % 10 == 0) ? "XXX" : (i % 3 == 0 ? "USD" : (i % 2 == 0 ? "EUR" : "KGS"));
                String amount = String.valueOf(100 + i * 10);
                String terminal = "TERM-00" + (1 + (i % 3));
                txs.add(createTx(date, terminal, curr, amount, "411122XXXXXX" + (1000 + i)));
            }

            response.setContent(txs);
            response.setTotalPages(1);
        } else {
            response.setTotalPages(1);
        }

        return response;
    }

    private MpcTransactionDto createTx(LocalDate date, String device, String curr, String amount, String card) {
        MpcTransactionDto dto = new MpcTransactionDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setDeviceCode(device);
        dto.setOperDateTime(date.atTime(12, 0).plusMinutes((long) (Math.random() * 300)));
        dto.setCurrency(curr);
        dto.setAmount(new BigDecimal(amount));
        dto.setCardNumber(card);
        return dto;
    }
}
