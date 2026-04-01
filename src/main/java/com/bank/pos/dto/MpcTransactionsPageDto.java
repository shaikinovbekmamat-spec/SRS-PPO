package com.bank.pos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MpcTransactionsPageDto {
    @JsonProperty("content")
    private List<MpcTransactionDto> content = new ArrayList<>();

    @JsonProperty("totalPages")
    private int totalPages;

    public List<MpcTransactionDto> getContent() {
        return content;
    }

    public void setContent(List<MpcTransactionDto> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

