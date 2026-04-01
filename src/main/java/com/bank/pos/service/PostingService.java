package com.bank.pos.service;

import com.bank.pos.entity.BankAccount;
import com.bank.pos.entity.Posting;
import com.bank.pos.entity.PostingStatus;
import com.bank.pos.entity.PosTransaction;
import com.bank.pos.entity.TransactionStatus;
import com.bank.pos.repository.BankAccountRepository;
import com.bank.pos.repository.PostingRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostingService {
    private final PostingRepository postingRepository;
    private final BankAccountRepository bankAccountRepository;

    public PostingService(PostingRepository postingRepository, BankAccountRepository bankAccountRepository) {
        this.postingRepository = postingRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Transactional
    public void createPostingFor(PosTransaction tx) {
        Posting p = new Posting();
        p.setTransaction(tx);
        p.setCurrency(tx.getCurrency());
        p.setAmount(tx.getAmount());

        try {
            BankAccount account = bankAccountRepository.findByCurrency(tx.getCurrency())
                    .orElseThrow(() -> new IllegalStateException("Не найден банковский счёт для валюты " + tx.getCurrency()));
            p.setBankAccount(account);
            p.setPostingStatus(PostingStatus.COMPLETED);
            p.setErrorMessage(null);

            postingRepository.save(p);
            tx.setStatus(TransactionStatus.PROCESSED);
        } catch (Exception e) {
            p.setPostingStatus(PostingStatus.FAILED);
            p.setErrorMessage(e.getMessage());
            try {
                // still try to persist a failed posting (for audit), if possible
                if (p.getBankAccount() != null) {
                    postingRepository.save(p);
                }
            } catch (DataIntegrityViolationException ignore) {
                // already exists
            }
            tx.setStatus(TransactionStatus.ERROR);
        }
    }
}

