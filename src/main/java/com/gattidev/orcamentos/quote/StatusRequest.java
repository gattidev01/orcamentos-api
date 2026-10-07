package com.gattidev.orcamentos.quote;

import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull QuoteStatus status) {
}
