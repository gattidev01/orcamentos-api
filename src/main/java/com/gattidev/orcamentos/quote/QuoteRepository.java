package com.gattidev.orcamentos.quote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QuoteRepository extends JpaRepository<Quote, UUID> {

    @EntityGraph(attributePaths = "items")
    Page<Quote> findAllByOwnerEmail(String email, Pageable pageable);

    @EntityGraph(attributePaths = "items")
    Optional<Quote> findByIdAndOwnerEmail(UUID id, String email);
}
