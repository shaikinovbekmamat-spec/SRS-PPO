package com.bank.pos.repository;

import com.bank.pos.entity.PosTerminal;
import com.bank.pos.entity.PosTerminalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PosTerminalRepository extends JpaRepository<PosTerminal, Long> {
    Optional<PosTerminal> findByDeviceCode(String deviceCode);
    long countByStatus(PosTerminalStatus status);
}

