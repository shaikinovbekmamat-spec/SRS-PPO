package com.bank.pos.service;

import com.bank.pos.dto.MpcTransactionDto;
import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.entity.PosTerminal;
import com.bank.pos.entity.PosTransaction;
import com.bank.pos.entity.TransactionStatus;
import com.bank.pos.repository.PosTransactionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionProcessor {
    private final PosTransactionRepository transactionRepository;
    private final PosTerminalService terminalService;
    private final CardMaskingService cardMaskingService;
    private final PostingService postingService;

    public TransactionProcessor(
            PosTransactionRepository transactionRepository,
            PosTerminalService terminalService,
            CardMaskingService cardMaskingService,
            PostingService postingService
    ) {
        this.transactionRepository = transactionRepository;
        this.terminalService = terminalService;
        this.cardMaskingService = cardMaskingService;
        this.postingService = postingService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean process(MpcTransactionDto dto) {
        if (dto.getId() == null || dto.getId().isBlank()) {
            return false;
        }
        if (transactionRepository.existsByMpcTransactionId(dto.getId())) {
            return false;
        }

        PosTerminal terminal = terminalService.getOrCreate(dto.getDeviceCode());
        PosTransaction tx = new PosTransaction();
        tx.setMpcTransactionId(dto.getId());
        tx.setTerminal(terminal);
        tx.setOperDateTime(dto.getOperDateTime());
        tx.setAmount(dto.getAmount());
        tx.setCardNumberMasked(cardMaskingService.mask(dto.getCardNumber()));
        tx.setStatus(TransactionStatus.NEW);

        // Безопасная проверка валюты
        try {
            tx.setCurrency(CurrencyCode.valueOf(dto.getCurrency()));
        } catch (Exception e) {
            tx.setCurrency(CurrencyCode.KGS); 
            tx.setStatus(TransactionStatus.ERROR);
            transactionRepository.save(tx);
            return true;
        }

        try {
            transactionRepository.save(tx);
            postingService.createPostingFor(tx);
            return true;
        } catch (DataIntegrityViolationException ignore) {
            return false;
        }
    }
}
