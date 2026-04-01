package com.bank.pos.scheduler;

import com.bank.pos.config.MpcProperties;
import com.bank.pos.service.MpcPollingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MpcPollingScheduler {
    private final MpcPollingService pollingService;
    private final MpcProperties props;

    public MpcPollingScheduler(MpcPollingService pollingService, MpcProperties props) {
        this.pollingService = pollingService;
        this.props = props;
    }

    @Scheduled(cron = "${app.mpc.polling.cron}")
    public void pollScheduled() {
        if (!props.getPolling().isEnabled()) {
            return;
        }
        pollingService.poll(LocalDate.now());
    }
}

