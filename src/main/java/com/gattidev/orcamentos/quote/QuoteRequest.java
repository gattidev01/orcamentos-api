package com.gattidev.orcamentos.quote;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuoteRequest(
        @NotBlank @Size(max = 120) String customerName,
        @NotBlank @Size(max = 120) String vehicle,
        @NotBlank @Size(max = 1000) String problemDescription,
        @NotEmpty @Size(max = 50) List<@Valid QuoteItemRequest> items
) {
}
