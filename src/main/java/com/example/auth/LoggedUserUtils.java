package com.example.auth;

import com.example.users.User;
import com.example.users.UserService;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

public class LoggedUserUtils {

    private LoggedUserUtils() {
        // Private constructor to prevent instantiation
    }

    public static User getLoggedUserDetails(AuthenticationFacade authenticationFacade, UserService userService) throws Exception {
        String userId = authenticationFacade.getJwtTokenPayload().id();
        Optional<User> foundUser = userService.findOneById(userId);
        return foundUser.orElseThrow(() -> new EmptyResultDataAccessException("User not found", 1));
    }
}