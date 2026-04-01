package com.bank.pos.repository.spec;

import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.entity.PosTransaction;
import com.bank.pos.entity.TransactionStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class PosTransactionSpecifications {
    private PosTransactionSpecifications() {
    }

    public static Specification<PosTransaction> operDateBetween(LocalDate from, LocalDate to) {
        if (from == null && to == null) {
            return Specification.where(null);
        }
        LocalDateTime fromDt = from == null ? null : from.atStartOfDay();
        LocalDateTime toExclusive = to == null ? null : to.plusDays(1).atStartOfDay();

        return (root, query, cb) -> {
            if (fromDt != null && toExclusive != null) {
                return cb.and(
                        cb.greaterThanOrEqualTo(root.get("operDateTime"), fromDt),
                        cb.lessThan(root.get("operDateTime"), toExclusive)
                );
            }
            if (fromDt != null) {
                return cb.greaterThanOrEqualTo(root.get("operDateTime"), fromDt);
            }
            return cb.lessThan(root.get("operDateTime"), toExclusive);
        };
    }

    public static Specification<PosTransaction> deviceCodeEquals(String deviceCode) {
        if (deviceCode == null || deviceCode.isBlank()) {
            return Specification.where(null);
        }
        return (root, query, cb) -> cb.equal(root.join("terminal").get("deviceCode"), deviceCode.trim());
    }

    public static Specification<PosTransaction> currencyEquals(CurrencyCode currency) {
        if (currency == null) {
            return Specification.where(null);
        }
        return (root, query, cb) -> cb.equal(root.get("currency"), currency);
    }

    public static Specification<PosTransaction> statusEquals(TransactionStatus status) {
        if (status == null) {
            return Specification.where(null);
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}

