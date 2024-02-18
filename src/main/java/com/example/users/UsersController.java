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

    @GetMapping("/token-info")
    public JwtTokenPayload getTokenInfo() throws Exception {
        return authenticationFacade.getJwtTokenPayload();
    }
    @GetMapping("/profile")
    public Optional<User> getProfile() throws Exception {
        return this.service.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
    }
    @GetMapping("/your-characters-ids")
    public List<String> getCharactersIds() throws Exception {
        Optional<User> user = this.service.findOneById(this.authenticationFacade.getJwtTokenPayload().id());

        return user.map(value -> value.getCharacters().stream().map((character->character.getId().toString())).toList()).orElse(null);

    }
    @GetMapping("users/{id}")
    public Optional<User> getUserById(@PathVariable String id){
        return service.findOneById(id);
    }
}
