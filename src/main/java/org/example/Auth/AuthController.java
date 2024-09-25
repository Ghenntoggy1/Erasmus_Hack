package org.example.Auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest userLogin) throws IllegalAccessException {
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                userLogin.username(),
                                userLogin.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthUser userDetails = (AuthUser) authentication.getPrincipal();


        log.info("Token requested for user :" + userDetails);
        String token = authService.generateToken(authentication);
        AuthDTO.Response response = new AuthDTO.Response("User logged in successfully", token);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // Set HTTP response status to 201 (Created)
    public ResponseEntity<?> register(@RequestBody AuthDTO.RegisterRequest registerRequest) {
        try {
            authService.registerUser(registerRequest.username(), registerRequest.password());
            return ResponseEntity.ok(new AuthDTO.Response("User registered successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthDTO.Response(e.getMessage(), null));
        }
    }
}