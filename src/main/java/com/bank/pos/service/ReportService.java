package com.bank.pos.service;

import com.bank.pos.dto.ReportResultDto;
import com.bank.pos.dto.ReportRowDto;
import com.bank.pos.dto.TerminalReportRowDto;
import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.entity.PosTransaction;
import com.bank.pos.repository.PosTransactionRepository;
import com.bank.pos.repository.spec.PosTransactionSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private final PosTransactionRepository transactionRepository;
    private final ExchangeRateService exchangeRateService;

    public ReportService(PosTransactionRepository transactionRepository, ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Transactional(readOnly = true)
    public ReportResultDto buildReport(LocalDate from, LocalDate to) {
        Specification<PosTransaction> spec = Specification.where(PosTransactionSpecifications.operDateBetween(from, to));
        List<PosTransaction> txs = transactionRepository.findAll(spec);

        Map<CurrencyCode, BigDecimal> byCurrency = new HashMap<>();
        Map<String, TerminalAgg> byTerminal = new HashMap<>();

        for (PosTransaction tx : txs) {
            try {
                CurrencyCode.valueOf(tx.getCurrency().name());
            } catch (Exception e) {
                continue; // Skip invalid currency
            }
            byCurrency.merge(tx.getCurrency(), tx.getAmount(), BigDecimal::add);

            String key = tx.getTerminal().getDeviceCode() + "|" + tx.getCurrency();
            byTerminal.computeIfAbsent(key, k -> new TerminalAgg(tx.getTerminal().getDeviceCode(), tx.getCurrency()))
                    .add(tx.getAmount());
        }

        ReportResultDto result = new ReportResultDto();
        List<ReportRowDto> currencyRows = new ArrayList<>();
        BigDecimal totalKgs = BigDecimal.ZERO;

        LocalDate rateDate = (to != null ? to : (from != null ? from : LocalDate.now()));

        for (Map.Entry<CurrencyCode, BigDecimal> e : byCurrency.entrySet()) {
            ReportRowDto row = new ReportRowDto(e.getKey());
            row.setAmount(e.getValue());
            BigDecimal rate = exchangeRateService.getRateToKgs(rateDate, e.getKey());
            BigDecimal kgs = e.getValue().multiply(rate).setScale(2, RoundingMode.HALF_UP);
            row.setAmountInKgs(kgs);
            totalKgs = totalKgs.add(kgs);
            currencyRows.add(row);
        }

        currencyRows.sort(Comparator.comparing(r -> r.getCurrency().name()));
        result.setByCurrency(currencyRows);

        List<TerminalReportRowDto> terminalRows = new ArrayList<>();
        for (TerminalAgg agg : byTerminal.values()) {
            TerminalReportRowDto r = new TerminalReportRowDto();
            r.setDeviceCode(agg.deviceCode);
            r.setCurrency(agg.currency);
            r.setCount(agg.count);
            r.setAmount(agg.amount);
            terminalRows.add(r);
        }
        terminalRows.sort(Comparator.comparing(TerminalReportRowDto::getDeviceCode).thenComparing(tr -> tr.getCurrency().name()));
        result.setByTerminal(terminalRows);

        result.setTotalInKgs(totalKgs.setScale(2, RoundingMode.HALF_UP));
        return result;
    }

    private static class TerminalAgg {
        private final String deviceCode;
        private final CurrencyCode currency;
        private long count;
        private BigDecimal amount = BigDecimal.ZERO;

        private TerminalAgg(String deviceCode, CurrencyCode currency) {
            this.deviceCode = deviceCode;
            this.currency = currency;
        }

        private void add(BigDecimal v) {
            count++;
            amount = amount.add(v);
        }
    }
}
