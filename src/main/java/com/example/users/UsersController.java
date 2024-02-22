package com.example.users;

import com.example.auth.AuthenticationFacade;
import com.example.auth.JwtTokenPayload;
import com.example.auth.LoggedUserUtils;
import com.example.auth.SecuredRestController;
import com.example.common.ResourceNotFoundException;
import com.example.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public CustomResponse<List<User>> findAll() {
        return new CustomResponse<>(HttpStatus.OK, this.service.findAll());
    }

    @GetMapping("/token-info")
    public CustomResponse<JwtTokenPayload> getTokenInfo() throws Exception {
        return new CustomResponse<>(HttpStatus.OK, authenticationFacade.getJwtTokenPayload());
    }
    @GetMapping("/profile")
    public CustomResponse<User> getProfile() throws Exception {
        return new CustomResponse<>(HttpStatus.OK,
                LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.service));
    }
    @GetMapping("/your-characters-ids")
    public CustomResponse<List<String>> getCharactersIds() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.service);

        List<String> charactersIds =loggedUser.getCharacters().stream().map((character->character.getId().toString())).toList();
        return new CustomResponse<>(HttpStatus.OK, charactersIds);
    }
    @GetMapping("users/{id}")
    public CustomResponse<User> getUserById(@PathVariable String id){
        Optional<User> userDB = this.service.findOneById(id);

       return userDB.map((user)->new CustomResponse<>(HttpStatus.OK,user))
                .orElseThrow(()->new ResourceNotFoundException("User", id));
    }
}
