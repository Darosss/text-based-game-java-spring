package com.example.auth;

import com.example.common.ResourceNotFoundException;
import com.example.users.User;
import com.example.users.UserService;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

public class LoggedUserUtils {

    private LoggedUserUtils() {
    }

    public static User getLoggedUserDetails(AuthenticationFacade authenticationFacade, UserService userService) throws Exception {
        String userId = authenticationFacade.getJwtTokenPayload().id();
        Optional<User> foundUser = userService.findOneById(userId);
        return foundUser.orElseThrow(() -> new ResourceNotFoundException("Not found your user details. Please try again later"));
    }
}