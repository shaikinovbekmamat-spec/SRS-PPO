package com.bank.pos.service;

import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.entity.ExchangeRate;
import com.bank.pos.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<ExchangeRate> findAllRates() {
        return exchangeRateRepository.findAllByOrderByRateDateDescFromCurrencyAsc();
    }

    public List<CurrencyCode> getManageableCurrencies() {
        return Arrays.stream(CurrencyCode.values())
                .filter(currency -> currency != CurrencyCode.KGS)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveRate(CurrencyCode fromCurrency, LocalDate rateDate, BigDecimal rate) {
        validateRateInput(fromCurrency, rateDate, rate);

        ExchangeRate exchangeRate = exchangeRateRepository
                .findByFromCurrencyAndRateDate(fromCurrency, rateDate)
                .orElseGet(ExchangeRate::new);

        exchangeRate.setFromCurrency(fromCurrency);
        exchangeRate.setRateDate(rateDate);
        exchangeRate.setRate(rate);
        exchangeRateRepository.save(exchangeRate);
    }

    @Transactional
    public void deleteRate(Long id) {
        exchangeRateRepository.deleteById(id);
    }

    private void validateRateInput(CurrencyCode fromCurrency, LocalDate rateDate, BigDecimal rate) {
        if (fromCurrency == null) {
            throw new IllegalArgumentException("Валюта обязательна");
        }
        if (fromCurrency == CurrencyCode.KGS) {
            throw new IllegalArgumentException("Для KGS курс всегда равен 1");
        }
        if (rateDate == null) {
            throw new IllegalArgumentException("Дата курса обязательна");
        }
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Курс должен быть больше нуля");
        }
    }
}
