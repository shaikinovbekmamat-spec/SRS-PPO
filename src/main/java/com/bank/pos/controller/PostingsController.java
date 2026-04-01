package com.bank.pos.controller;

import com.bank.pos.entity.CurrencyCode;
import com.bank.pos.entity.Posting;
import com.bank.pos.entity.PostingStatus;
import com.bank.pos.repository.PostingRepository;
import com.bank.pos.repository.spec.PostingSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class PostingsController {
    private final PostingRepository postingRepository;

    public PostingsController(PostingRepository postingRepository) {
        this.postingRepository = postingRepository;
    }

    @GetMapping("/postings")
    public String postings(
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(value = "currency", required = false) CurrencyCode currency,
            @RequestParam(value = "status", required = false) PostingStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model
    ) {
        Specification<Posting> spec = Specification.where(PostingSpecifications.createdAtBetween(from, to))
                .and(PostingSpecifications.currencyEquals(currency))
                .and(PostingSpecifications.statusEquals(status));

        Page<Posting> postingPage = postingRepository.findAll(
                spec,
                PageRequest.of(Math.max(page, 0), 20, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        model.addAttribute("postingPage", postingPage);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("currency", currency);
        model.addAttribute("status", status);
        model.addAttribute("currencies", CurrencyCode.values());
        model.addAttribute("statuses", PostingStatus.values());
        return "postings";
    }
}

