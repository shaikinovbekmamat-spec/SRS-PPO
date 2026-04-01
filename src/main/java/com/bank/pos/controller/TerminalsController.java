package com.bank.pos.controller;

import com.bank.pos.entity.PosTerminal;
import com.bank.pos.entity.PosTerminalStatus;
import com.bank.pos.entity.PosTransaction;
import com.bank.pos.repository.PosTerminalRepository;
import com.bank.pos.repository.PosTransactionRepository;
import com.bank.pos.service.PosTerminalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TerminalsController {
    private final PosTerminalRepository terminalRepository;
    private final PosTransactionRepository transactionRepository;
    private final PosTerminalService terminalService;

    public TerminalsController(PosTerminalRepository terminalRepository, 
                               PosTransactionRepository transactionRepository,
                               PosTerminalService terminalService) {
        this.terminalRepository = terminalRepository;
        this.transactionRepository = transactionRepository;
        this.terminalService = terminalService;
    }

    @GetMapping("/terminals")
    public String terminals(Model model) {
        List<PosTerminal> terminals = terminalRepository.findAll();
        Map<Long, PosTransaction> lastTxByTerminalId = new HashMap<>();
        for (PosTerminal t : terminals) {
            PosTransaction last = transactionRepository.findTop1ByTerminal_IdOrderByOperDateTimeDesc(t.getId());
            if (last != null) {
                lastTxByTerminalId.put(t.getId(), last);
            }
        }

        model.addAttribute("terminals", terminals);
        model.addAttribute("lastTxByTerminalId", lastTxByTerminalId);
        return "terminals";
    }

    @PostMapping("/terminals/add")
    public String addTerminal(@RequestParam("deviceCode") String deviceCode) {
        terminalService.getOrCreate(deviceCode);
        return "redirect:/terminals";
    }

    @PostMapping("/terminals/update-status")
    public String updateStatus(@RequestParam("id") Long id, @RequestParam("status") PosTerminalStatus status) {
        PosTerminal terminal = terminalService.getById(id);
        if (terminal != null) {
            terminal.setStatus(status);
            terminalService.save(terminal);
        }
        return "redirect:/terminals";
    }

    @PostMapping("/terminals/delete/{id}")
    public String deleteTerminal(@PathVariable("id") Long id) {
        terminalService.delete(id);
        return "redirect:/terminals";
    }
}
