package com.bank.pos.repository.spec;

import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.entity.Posting;
import com.bank.pos.entity.PostingStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class PostingSpecifications {
    private PostingSpecifications() {
    }

    public static Specification<Posting> createdAtBetween(LocalDate from, LocalDate to) {
        if (from == null && to == null) {
            return Specification.where(null);
        }
        LocalDateTime fromDt = from == null ? null : from.atStartOfDay();
        LocalDateTime toExclusive = to == null ? null : to.plusDays(1).atStartOfDay();

        return (root, query, cb) -> {
            if (fromDt != null && toExclusive != null) {
                return cb.and(
                        cb.greaterThanOrEqualTo(root.get("createdAt"), fromDt),
                        cb.lessThan(root.get("createdAt"), toExclusive)
                );
            }
            if (fromDt != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), fromDt);
            }
            return cb.lessThan(root.get("createdAt"), toExclusive);
        };
    }

    public static Specification<Posting> currencyEquals(CurrencyCode currency) {
        if (currency == null) {
            return Specification.where(null);
        }
        return (root, query, cb) -> cb.equal(root.get("currency"), currency);
    }

    public static Specification<Posting> statusEquals(PostingStatus status) {
        if (status == null) {
            return Specification.where(null);
        }
        return (root, query, cb) -> cb.equal(root.get("postingStatus"), status);
    }
}

