package com.bank.pos.service;

import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public BigDecimal getRateToKgs(LocalDate date, CurrencyCode fromCurrency) {
        if (fromCurrency == CurrencyCode.KGS) {
            return BigDecimal.ONE;
        }
        
        // Try specific date
        Optional<BigDecimal> rateOnDate = exchangeRateRepository.findByFromCurrencyAndRateDate(fromCurrency, date)
                .map(r -> r.getRate());
        
        if (rateOnDate.isPresent()) {
            return rateOnDate.get();
        }

        // Fallback to latest available rate
        return exchangeRateRepository.findFirstByFromCurrencyOrderByRateDateDesc(fromCurrency)
                .map(r -> r.getRate())
                .orElse(BigDecimal.ONE); // Final fallback to avoid crash
    }
}

