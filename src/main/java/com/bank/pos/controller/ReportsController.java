package com.bank.pos.controller;

import com.bank.pos.dto.ReportResultDto;
import com.bank.pos.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Controller
public class ReportsController {
    private final ReportService reportService;

    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports")
    public String reports(
            @RequestParam(value = "preset", required = false) String preset,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model
    ) {
        LocalDate today = LocalDate.now();
        if (from == null || to == null) {
            if (preset == null) preset = "today";
        }

        if ("today".equalsIgnoreCase(preset)) {
            from = today;
            to = today;
        } else if ("week".equalsIgnoreCase(preset)) {
            from = today.with(DayOfWeek.MONDAY);
            to = today;
        } else if ("month".equalsIgnoreCase(preset)) {
            from = today.withDayOfMonth(1);
            to = today;
        } else if (from == null) {
            from = today;
            to = today;
        }

        ReportResultDto report = reportService.buildReport(from, to);

        model.addAttribute("preset", preset);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("report", report);
        return "reports";
    }
}

