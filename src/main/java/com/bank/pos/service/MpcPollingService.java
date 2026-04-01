package com.bank.pos.service;

import com.bank.pos.client.MpcApiClient;
import com.bank.pos.dto.MpcTransactionDto;
import com.bank.pos.entity.MpcPollRun;
import com.bank.pos.entity.MpcPollRunStatus;
import com.bank.pos.repository.MpcPollRunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MpcPollingService {
    private final MpcApiClient apiClient;
    private final TransactionService transactionService;
    private final MpcPollRunRepository pollRunRepository;

    public MpcPollingService(MpcApiClient apiClient, TransactionService transactionService, MpcPollRunRepository pollRunRepository) {
        this.apiClient = apiClient;
        this.transactionService = transactionService;
        this.pollRunRepository = pollRunRepository;
    }

    @Transactional
    public MpcPollRun poll(LocalDate date) {
        MpcPollRun run = new MpcPollRun();
        run.setRequestedDate(date);
        run.setStatus(MpcPollRunStatus.RUNNING);
        run = pollRunRepository.save(run);

        try {
            List<MpcTransactionDto> mpcTxs = apiClient.fetchTransactions(date);
            run.setReceivedCount(mpcTxs.size());
            transactionService.ingestFromMpc(date, mpcTxs);
            run.setStatus(MpcPollRunStatus.SUCCESS);
        } catch (Exception e) {
            run.setStatus(MpcPollRunStatus.FAILED);
            run.setErrorMessage(e.getMessage());
        } finally {
            run.setFinishedAt(LocalDateTime.now());
        }

        return pollRunRepository.save(run);
    }
}

