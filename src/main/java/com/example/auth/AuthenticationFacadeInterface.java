package com.example.auth;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeInterface {
    Authentication getAuthentication();
}
