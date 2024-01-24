package com.example.auth;

import com.example.response.ErrorResponse;
import com.example.users.CustomUserDetails;
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
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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

            System.out.println("TOKEN : "+token);
            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}