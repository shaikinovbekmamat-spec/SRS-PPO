package com.bank.pos.repository;

import com.bank.pos.entity.MpcPollRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MpcPollRunRepository extends JpaRepository<MpcPollRun, Long> {
    Optional<MpcPollRun> findTopByOrderByStartedAtDesc();
}

