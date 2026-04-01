package com.bank.pos.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReportResultDto {
    private List<ReportRowDto> byCurrency = new ArrayList<>();
    private List<TerminalReportRowDto> byTerminal = new ArrayList<>();
    private BigDecimal totalInKgs = BigDecimal.ZERO;

    public List<ReportRowDto> getByCurrency() {
        return byCurrency;
    }

    public void setByCurrency(List<ReportRowDto> byCurrency) {
        this.byCurrency = byCurrency;
    }

    public List<TerminalReportRowDto> getByTerminal() {
        return byTerminal;
    }

    public void setByTerminal(List<TerminalReportRowDto> byTerminal) {
        this.byTerminal = byTerminal;
    }

    public BigDecimal getTotalInKgs() {
        return totalInKgs;
    }

    public void setTotalInKgs(BigDecimal totalInKgs) {
        this.totalInKgs = totalInKgs;
    }
}

