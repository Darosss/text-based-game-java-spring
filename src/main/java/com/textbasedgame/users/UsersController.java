package com.textbasedgame.users;

import com.textbasedgame.auth.AuthenticationFacade;
import com.textbasedgame.auth.JwtTokenPayload;
import com.textbasedgame.auth.LoggedUserUtils;
import com.textbasedgame.auth.SecuredRestController;
import com.textbasedgame.common.ResourceNotFoundException;
import com.textbasedgame.response.CustomResponse;
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
    public CustomResponse<LoggedUserUtils.ProfileUserDetails> getProfile() throws Exception {
        return new CustomResponse<>(HttpStatus.OK,
                LoggedUserUtils.getProfileUserDetails(this.authenticationFacade, this.service));
    }
    @GetMapping("users/{id}")
    public CustomResponse<User> getUserById(@PathVariable String id){
        Optional<User> userDB = this.service.findOneById(id);

       return userDB.map((user)->new CustomResponse<>(HttpStatus.OK,user))
                .orElseThrow(()->new ResourceNotFoundException("User", id));
    }
}
