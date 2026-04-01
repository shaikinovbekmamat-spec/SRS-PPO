package com.bank.pos.repository;

import com.bank.pos.entity.BankAccount;
import com.bank.pos.entity.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByCurrency(CurrencyCode currency);
}

