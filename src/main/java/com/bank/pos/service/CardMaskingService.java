package com.bank.pos.service;

import org.springframework.stereotype.Service;

@Service
public class CardMaskingService {
    public String mask(String raw) {
        if (raw == null || raw.isBlank()) {
            return "******0000";
        }
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() < 10) {
            // Already masked / unknown format; store as-is but truncate to keep DB clean
            return raw.length() > 32 ? raw.substring(0, 32) : raw;
        }
        String first6 = digits.substring(0, 6);
        String last4 = digits.substring(digits.length() - 4);
        return first6 + "******" + last4;
    }
}

