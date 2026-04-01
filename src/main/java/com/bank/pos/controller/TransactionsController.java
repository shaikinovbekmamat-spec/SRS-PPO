package com.bank.pos.controller;

import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.entity.PosTransaction;
import com.bank.pos.entity.TransactionStatus;
import com.bank.pos.repository.PosTransactionRepository;
import com.bank.pos.repository.spec.PosTransactionSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class TransactionsController {
    private final PosTransactionRepository transactionRepository;

    public TransactionsController(PosTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/transactions")
    public String transactions(
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(value = "deviceCode", required = false) String deviceCode,
            @RequestParam(value = "currency", required = false) CurrencyCode currency,
            @RequestParam(value = "status", required = false) TransactionStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model
    ) {
        Specification<PosTransaction> spec = Specification.where(PosTransactionSpecifications.operDateBetween(from, to))
                .and(PosTransactionSpecifications.deviceCodeEquals(deviceCode))
                .and(PosTransactionSpecifications.currencyEquals(currency))
                .and(PosTransactionSpecifications.statusEquals(status));

        Page<PosTransaction> txPage = transactionRepository.findAll(
                spec,
                PageRequest.of(Math.max(page, 0), 20, Sort.by(Sort.Direction.DESC, "operDateTime"))
        );

        model.addAttribute("txPage", txPage);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("deviceCode", deviceCode);
        model.addAttribute("currency", currency);
        model.addAttribute("status", status);
        model.addAttribute("currencies", CurrencyCode.values());
        model.addAttribute("statuses", TransactionStatus.values());
        return "transactions";
    }
}

