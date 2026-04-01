package com.bank.pos.controller;

import com.bank.pos.service.MpcPollingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class MpcController {
    private final MpcPollingService pollingService;

    public MpcController(MpcPollingService pollingService) {
        this.pollingService = pollingService;
    }

    @PostMapping("/mpc/poll")
    public String poll(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        pollingService.poll(date);
        return "redirect:/dashboard";
    }
}

