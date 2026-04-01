package com.bank.pos.dto;

import com.bank.pos.entity.CurrencyCode;

import java.math.BigDecimal;

public class TerminalReportRowDto {
    private String deviceCode;
    private CurrencyCode currency;
    private long count;
    private BigDecimal amount = BigDecimal.ZERO;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public CurrencyCode getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyCode currency) {
        this.currency = currency;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

