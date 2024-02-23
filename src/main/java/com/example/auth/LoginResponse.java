package com.example.auth;

import java.util.Date;

public class LoginResponse {
    private String email;
    private String token;
    private Date expirationTime;

    public LoginResponse(String email, String token, Date expirationTime) {
        this.email = email;
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }
}