package com.example.auth;

public record JwtTokenPayload(String id, String email, String username) {
    public JwtTokenPayload(String id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
}
