package com.gattidev.orcamentos.quote;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuoteResponse create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody QuoteRequest request) {
        return quoteService.create(jwt.getSubject(), request);
    }

    @GetMapping
    public Page<QuoteResponse> list(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return quoteService.list(jwt.getSubject(), pageable);
    }

    @GetMapping("/{id}")
    public QuoteResponse get(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        return quoteService.get(jwt.getSubject(), id);
    }

    @PutMapping("/{id}")
    public QuoteResponse update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id,
            @Valid @RequestBody QuoteRequest request
    ) {
        return quoteService.update(jwt.getSubject(), id, request);
    }

    @PatchMapping("/{id}/status")
    public QuoteResponse changeStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id,
            @Valid @RequestBody StatusRequest request
    ) {
        return quoteService.changeStatus(jwt.getSubject(), id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        quoteService.delete(jwt.getSubject(), id);
    }
}
