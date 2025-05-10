package com.textbasedgame.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements AuthenticationFacadeInterface {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public JwtTokenPayload getJwtTokenPayload() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String tokenDataAsString = authentication.getName();
            if (tokenDataAsString != null) {
                return new ObjectMapper().readValue(tokenDataAsString, JwtTokenPayload.class);
            }
        }
        // Handle the case where authentication or tokenDataAsString is null

        throw new Exception("Authentication or token data is not available."                );
    }
}
