package com.bank.pos.service;

import com.bank.pos.dto.MpcTransactionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionProcessor transactionProcessor;

    public TransactionService(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    @Transactional
    public int ingestFromMpc(LocalDate requestedDate, List<MpcTransactionDto> mpcTransactions) {
        int saved = 0;
        for (MpcTransactionDto dto : mpcTransactions) {
            try {
                if (transactionProcessor.process(dto)) {
                    saved++;
                }
            } catch (Exception e) {
                // Ignore errors for individual transactions to allow others to be processed
            }
        }
        return saved;
    }
}
