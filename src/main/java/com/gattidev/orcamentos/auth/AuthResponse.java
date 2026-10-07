package com.gattidev.orcamentos.auth;

public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds) {
}
