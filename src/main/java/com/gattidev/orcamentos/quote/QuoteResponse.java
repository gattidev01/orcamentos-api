package com.gattidev.orcamentos.quote;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record QuoteResponse(
        UUID id,
        String customerName,
        String vehicle,
        String problemDescription,
        List<QuoteItemResponse> items,
        BigDecimal total,
        QuoteStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    static QuoteResponse from(Quote quote) {
        return new QuoteResponse(
                quote.getId(),
                quote.getCustomerName(),
                quote.getVehicle(),
                quote.getProblemDescription(),
                quote.getItems().stream().map(QuoteItemResponse::from).toList(),
                quote.total(),
                quote.getStatus(),
                quote.getCreatedAt(),
                quote.getUpdatedAt()
        );
    }
}
