package com.bank.pos.dto;

import com.bank.pos.entity.CurrencyCode;

import java.math.BigDecimal;

public class ReportRowDto {
    private CurrencyCode currency;
    private BigDecimal amount = BigDecimal.ZERO;
    private BigDecimal amountInKgs = BigDecimal.ZERO;

    public ReportRowDto() {
    }

    public ReportRowDto(CurrencyCode currency) {
        this.currency = currency;
    }

    public CurrencyCode getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyCode currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountInKgs() {
        return amountInKgs;
    }

    public void setAmountInKgs(BigDecimal amountInKgs) {
        this.amountInKgs = amountInKgs;
    }
}

