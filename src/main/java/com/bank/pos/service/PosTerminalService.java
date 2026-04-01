package com.bank.pos.service;

import com.bank.pos.entity.PosTerminal;
import com.bank.pos.entity.PosTerminalStatus;
import com.bank.pos.repository.PosTerminalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PosTerminalService {
    private final PosTerminalRepository terminalRepository;

    public PosTerminalService(PosTerminalRepository terminalRepository) {
        this.terminalRepository = terminalRepository;
    }

    @Transactional
    public void delete(Long id) {
        terminalRepository.deleteById(id);
    }

    @Transactional
    public void save(PosTerminal terminal) {
        terminalRepository.save(terminal);
    }

    public PosTerminal getById(Long id) {
        return terminalRepository.findById(id).orElse(null);
    }

    @Transactional
    public PosTerminal getOrCreate(String deviceCode) {
        return terminalRepository.findByDeviceCode(deviceCode)
                .orElseGet(() -> {
                    PosTerminal t = new PosTerminal();
                    t.setDeviceCode(deviceCode);
                    t.setStatus(PosTerminalStatus.ACTIVE);
                    return terminalRepository.save(t);
                });
    }
}

