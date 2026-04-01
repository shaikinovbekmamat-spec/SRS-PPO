package com.bank.pos.controller;

import com.bank.pos.entity.MpcPollRun;
import com.bank.pos.entity.PosTerminalStatus;
import com.bank.pos.entity.PosTransaction;
import com.bank.pos.repository.MpcPollRunRepository;
import com.bank.pos.repository.PostingRepository;
import com.bank.pos.repository.PosTerminalRepository;
import com.bank.pos.repository.PosTransactionRepository;
import com.bank.pos.service.ExchangeRateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DashboardController {
    private final PosTransactionRepository transactionRepository;
    private final PostingRepository postingRepository;
    private final PosTerminalRepository terminalRepository;
    private final MpcPollRunRepository pollRunRepository;
    private final ExchangeRateService exchangeRateService;

    public DashboardController(
            PosTransactionRepository transactionRepository,
            PostingRepository postingRepository,
            PosTerminalRepository terminalRepository,
            MpcPollRunRepository pollRunRepository,
            ExchangeRateService exchangeRateService
    ) {
        this.transactionRepository = transactionRepository;
        this.postingRepository = postingRepository;
        this.terminalRepository = terminalRepository;
        this.pollRunRepository = pollRunRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LocalDate today = LocalDate.now();
        LocalDateTime from = today.atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay();

        long txCountToday = transactionRepository.countByOperDateTimeBetween(from, to);
        long postingsToday = postingRepository.countByCreatedAtBetween(from, to);
        long activeTerminals = terminalRepository.countByStatus(PosTerminalStatus.ACTIVE);

        List<PosTransaction> lastTx = transactionRepository.findTop10ByOrderByOperDateTimeDesc();

        BigDecimal sumKgsToday = BigDecimal.ZERO;
        for (PosTransaction tx : transactionRepository.findByOperDateTimeBetween(from, to)) {
            BigDecimal rate = exchangeRateService.getRateToKgs(today, tx.getCurrency());
            sumKgsToday = sumKgsToday.add(tx.getAmount().multiply(rate));
        }

        MpcPollRun lastRun = pollRunRepository.findTopByOrderByStartedAtDesc().orElse(null);

        model.addAttribute("txCountToday", txCountToday);
        model.addAttribute("sumKgsToday", sumKgsToday.setScale(2, RoundingMode.HALF_UP));
        model.addAttribute("activeTerminals", activeTerminals);
        model.addAttribute("postingsToday", postingsToday);
        model.addAttribute("lastTx", lastTx);
        model.addAttribute("lastRun", lastRun);
        model.addAttribute("today", today);
        return "dashboard";
    }
}

