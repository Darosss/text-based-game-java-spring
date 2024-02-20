package com.example.auth;

import com.example.response.ErrorResponse;
import com.example.users.CreateUserDTO;
import com.example.users.CustomUserDetails;
import com.example.users.User;
import com.example.users.UserService;
import com.example.users.inventory.Inventory;
import com.example.users.inventory.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
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
    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq)  {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginReq.getEmail(), loginReq.getPassword())
                            );
            CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
            authentication.getDetails();

            String token = jwtUtil.createToken(user.getUser());
            LoginResponse loginRes = new LoginResponse(user.getUser().getEmail(), token);

            logger.debug("Token: {}", token);
            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        try{
            Inventory inventory = this.inventoryService.create(new Inventory(1000));
            CreateUserDTO createUserDto = new CreateUserDTO(
                    registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getUsername(), inventory
            );

            User createdUser =  this.userService.create(new User(createUserDto));
            inventory.setUser(createdUser);
            this.inventoryService.update(inventory);
            return ResponseEntity.ok(createdUser);
        }catch(Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
