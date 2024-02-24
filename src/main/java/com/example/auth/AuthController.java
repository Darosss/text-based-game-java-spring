package com.example.auth;

import com.example.response.CustomResponse;
import com.example.users.CreateUserDTO;
import com.example.users.CustomUserDetails;
import com.example.users.User;
import com.example.users.UserService;
import com.example.users.inventory.Inventory;
import com.example.users.inventory.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final InventoryService inventoryService;
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserService userService, InventoryService inventoryService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.inventoryService = inventoryService;
    }

    @ResponseBody()
    @PostMapping("auth/login")
    public CustomResponse<LoginResponse> login(@RequestBody LoginRequest loginReq) throws Exception {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginReq.getEmail(), loginReq.getPassword())
                            );
            CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
            authentication.getDetails();

            JwtUtil.CreateTokenReturn createTokenReturn = jwtUtil.createToken(user.getUser());
            LoginResponse loginRes = new LoginResponse(user.getUser().getEmail(), createTokenReturn.token(), createTokenReturn.expirationTime());

            logger.debug("Token: {}", createTokenReturn.token());
            return new CustomResponse<>(HttpStatus.OK, "Successfully logged in", loginRes);

        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ResponseBody
    @PostMapping("auth/register")
    public CustomResponse<User> register(@RequestBody RegisterRequest registerRequest) {
        Inventory inventory = this.inventoryService.create(new Inventory(1000));
        CreateUserDTO createUserDto = new CreateUserDTO(
                registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getUsername(), inventory
        );

        User createdUser =  this.userService.create(new User(createUserDto));
        inventory.setUser(createdUser);
        this.inventoryService.update(inventory);
        return new CustomResponse<>(HttpStatus.CREATED, "Successfully registered", createdUser);
    }
}
