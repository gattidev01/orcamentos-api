package com.gattidev.orcamentos.auth;

import com.gattidev.orcamentos.common.ConflictException;
import com.gattidev.orcamentos.user.AppUser;
import com.gattidev.orcamentos.user.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, authenticationManager, jwtService);
    }

    @Test
    void shouldNormalizeEmailAndHashPasswordWhenRegistering() {
        RegisterRequest request = new RegisterRequest("Natália", " NATALIA@EXAMPLE.COM ", "senha-segura");
        when(passwordEncoder.encode("senha-segura")).thenReturn("hashed");
        when(jwtService.createToken("natalia@example.com"))
                .thenReturn(new AuthResponse("token", "Bearer", 3600));

        AuthResponse response = authService.register(request);

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("natalia@example.com");
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("hashed");
        assertThat(response.accessToken()).isEqualTo("token");
    }

    @Test
    void shouldRejectDuplicatedEmail() {
        RegisterRequest request = new RegisterRequest("Natália", "natalia@example.com", "senha-segura");
        when(userRepository.existsByEmail("natalia@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Já existe uma conta com este e-mail");
    }
}
