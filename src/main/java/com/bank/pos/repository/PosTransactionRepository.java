package com.bank.pos.repository;

import com.bank.pos.entity.PosTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PosTransactionRepository extends JpaRepository<PosTransaction, Long>, JpaSpecificationExecutor<PosTransaction> {
    boolean existsByMpcTransactionId(String mpcTransactionId);
    Optional<PosTransaction> findByMpcTransactionId(String mpcTransactionId);

    long countByOperDateTimeBetween(LocalDateTime fromInclusive, LocalDateTime toExclusive);

    @EntityGraph(attributePaths = {"terminal"})
    List<PosTransaction> findTop10ByOrderByOperDateTimeDesc();

    @EntityGraph(attributePaths = {"terminal"})
    Page<PosTransaction> findAll(Specification<PosTransaction> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"terminal"})
    PosTransaction findTop1ByTerminal_IdOrderByOperDateTimeDesc(Long terminalId);

    @EntityGraph(attributePaths = {"terminal"})
    List<PosTransaction> findByOperDateTimeBetween(LocalDateTime fromInclusive, LocalDateTime toExclusive);
}

