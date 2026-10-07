package com.gattidev.orcamentos.quote;

import com.gattidev.orcamentos.common.NotFoundException;
import com.gattidev.orcamentos.user.AppUser;
import com.gattidev.orcamentos.user.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private AppUserRepository userRepository;

    private QuoteService quoteService;

    @BeforeEach
    void setUp() {
        quoteService = new QuoteService(quoteRepository, userRepository);
    }

    @Test
    void shouldCreateQuoteForAuthenticatedOwner() {
        AppUser owner = new AppUser("Natália", "natalia@example.com", "hash");
        QuoteRequest request = request();
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(quoteRepository.save(any(Quote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QuoteResponse response = quoteService.create(owner.getEmail(), request);

        assertThat(response.customerName()).isEqualTo("Cliente Teste");
        assertThat(response.total()).isEqualByComparingTo("250.00");
        assertThat(response.status()).isEqualTo(QuoteStatus.DRAFT);
        verify(quoteRepository).save(any(Quote.class));
    }

    @Test
    void shouldNotExposeAQuoteOwnedByAnotherUser() {
        UUID id = UUID.randomUUID();
        when(quoteRepository.findByIdAndOwnerEmail(id, "intruso@example.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> quoteService.get("intruso@example.com", id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Orçamento não encontrado");
    }

    private QuoteRequest request() {
        return new QuoteRequest(
                "Cliente Teste",
                "Fiat Uno",
                "Troca de pastilhas",
                List.of(new QuoteItemRequest(
                        "Serviço completo",
                        BigDecimal.ONE,
                        new BigDecimal("250.00")
                ))
        );
    }
}
