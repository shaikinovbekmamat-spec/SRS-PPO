package com.bank.pos.controller;

import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.service.ExchangeRateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/exchange-rates")
    public String exchangeRates(Model model) {
        model.addAttribute("rates", exchangeRateService.findAllRates());
        model.addAttribute("currencies", exchangeRateService.getManageableCurrencies());
        model.addAttribute("today", LocalDate.now());
        return "exchange-rates";
    }

    @PostMapping("/exchange-rates")
    public String saveRate(
            @RequestParam("currency") CurrencyCode currency,
            @RequestParam("rateDate") LocalDate rateDate,
            @RequestParam("rate") BigDecimal rate,
            RedirectAttributes redirectAttributes
    ) {
        try {
            exchangeRateService.saveRate(currency, rateDate, rate);
            redirectAttributes.addFlashAttribute("successMessage", "Курс успешно сохранён");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/exchange-rates";
    }

    @PostMapping("/exchange-rates/delete/{id}")
    public String deleteRate(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        exchangeRateService.deleteRate(id);
        redirectAttributes.addFlashAttribute("successMessage", "Курс удалён");
        return "redirect:/exchange-rates";
    }
}
