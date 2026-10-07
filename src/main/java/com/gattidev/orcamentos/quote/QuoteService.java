package com.gattidev.orcamentos.quote;

import com.gattidev.orcamentos.common.NotFoundException;
import com.gattidev.orcamentos.user.AppUser;
import com.gattidev.orcamentos.user.AppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final AppUserRepository userRepository;

    public QuoteService(QuoteRepository quoteRepository, AppUserRepository userRepository) {
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public QuoteResponse create(String ownerEmail, QuoteRequest request) {
        AppUser owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Quote quote = new Quote(
                owner,
                request.customerName().trim(),
                request.vehicle().trim(),
                request.problemDescription().trim(),
                toItems(request)
        );
        return QuoteResponse.from(quoteRepository.save(quote));
    }

    @Transactional(readOnly = true)
    public Page<QuoteResponse> list(String ownerEmail, Pageable pageable) {
        return quoteRepository.findAllByOwnerEmail(ownerEmail, pageable).map(QuoteResponse::from);
    }

    @Transactional(readOnly = true)
    public QuoteResponse get(String ownerEmail, UUID id) {
        return QuoteResponse.from(findOwnedQuote(ownerEmail, id));
    }

    @Transactional
    public QuoteResponse update(String ownerEmail, UUID id, QuoteRequest request) {
        Quote quote = findOwnedQuote(ownerEmail, id);
        quote.update(
                request.customerName().trim(),
                request.vehicle().trim(),
                request.problemDescription().trim(),
                toItems(request)
        );
        return QuoteResponse.from(quote);
    }

    @Transactional
    public QuoteResponse changeStatus(String ownerEmail, UUID id, QuoteStatus status) {
        Quote quote = findOwnedQuote(ownerEmail, id);
        quote.changeStatus(status);
        return QuoteResponse.from(quote);
    }

    @Transactional
    public void delete(String ownerEmail, UUID id) {
        quoteRepository.delete(findOwnedQuote(ownerEmail, id));
    }

    private Quote findOwnedQuote(String ownerEmail, UUID id) {
        return quoteRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new NotFoundException("Orçamento não encontrado"));
    }

    private List<QuoteItem> toItems(QuoteRequest request) {
        return request.items().stream().map(QuoteItemRequest::toEntity).toList();
    }
}
