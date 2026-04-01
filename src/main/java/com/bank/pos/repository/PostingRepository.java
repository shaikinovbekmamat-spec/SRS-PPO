package com.bank.pos.repository;

import com.bank.pos.entity.Posting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long>, JpaSpecificationExecutor<Posting> {
    long countByCreatedAtBetween(LocalDateTime fromInclusive, LocalDateTime toExclusive);

    @EntityGraph(attributePaths = {"transaction", "bankAccount", "transaction.terminal"})
    List<Posting> findTop50ByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"transaction", "bankAccount", "transaction.terminal"})
    Page<Posting> findAll(Specification<Posting> spec, Pageable pageable);
}

