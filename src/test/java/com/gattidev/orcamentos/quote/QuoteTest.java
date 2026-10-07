package com.gattidev.orcamentos.quote;

import com.gattidev.orcamentos.user.AppUser;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteTest {

    @Test
    void shouldCalculateTotalFromAllItems() {
        AppUser owner = new AppUser("Natália", "natalia@example.com", "hash");
        Quote quote = new Quote(
                owner,
                "Cliente",
                "Honda Civic",
                "Revisão preventiva",
                List.of(
                        new QuoteItem("Óleo", new BigDecimal("4.00"), new BigDecimal("35.50")),
                        new QuoteItem("Mão de obra", BigDecimal.ONE, new BigDecimal("180.00"))
                )
        );

        assertThat(quote.total()).isEqualByComparingTo("322.00");
        assertThat(quote.getStatus()).isEqualTo(QuoteStatus.DRAFT);
    }
}
