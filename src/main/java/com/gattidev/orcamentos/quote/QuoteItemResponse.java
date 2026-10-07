package com.gattidev.orcamentos.quote;

import java.math.BigDecimal;

public record QuoteItemResponse(
        String description,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
    static QuoteItemResponse from(QuoteItem item) {
        return new QuoteItemResponse(
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.subtotal()
        );
    }
}
