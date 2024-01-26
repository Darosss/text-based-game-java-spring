package com.example.users;

import com.example.auth.AuthenticationFacade;
import com.example.auth.JwtTokenPayload;
import com.example.auth.SecuredRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UsersController implements SecuredRestController {
    private final AuthenticationFacade authenticationFacade;
    private final UserService service;
    @Autowired()
    UsersController( AuthenticationFacade authenticationFacade,
                     UserService service) {
        this.authenticationFacade = authenticationFacade;
        this.service = service;
    }
    @GetMapping("/users")
    public List<User> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/profile")
    public JwtTokenPayload getProfile() throws Exception {
        return authenticationFacade.getJwtTokenPayload();
    }
    @GetMapping("users/{id}")
    public Optional<User> getUserById(@PathVariable String id){
        return service.findOneById(id);
    }
}
