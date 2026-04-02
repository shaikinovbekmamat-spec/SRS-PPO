package com.bank.pos.repository;

import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findByFromCurrencyAndRateDate(CurrencyCode fromCurrency, LocalDate rateDate);
    Optional<ExchangeRate> findFirstByFromCurrencyOrderByRateDateDesc(CurrencyCode fromCurrency);
    List<ExchangeRate> findAllByOrderByRateDateDescFromCurrencyAsc();
}
