package org.example.Auth;

import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import org.example.Security.MFA.MFAService;
import org.example.Security.MFA.TemporaryUserStore;
import org.example.User.Role.Role;
import org.example.User.User;
import org.example.User.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static io.jsonwebtoken.Jwts.header;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TemporaryUserStore temporaryUserStore;

    @Autowired
    private MFAService mfaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTO.LoginRequest userLogin) throws IllegalAccessException {
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                userLogin.username(),
                                userLogin.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthUser userDetails = (AuthUser) authentication.getPrincipal();
        User user = userRepository.findByUsername(userLogin.username()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isMfaEnabled()) {
            return ResponseEntity.ok(new AuthDTO.ResponseNoMFA("Enter MFA code", null)); // Indicate to enter MFA code
        }

        log.info("Token requested for user :{}", authentication.getAuthorities());
        String token = authService.generateToken(authentication);
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true) // Marks the cookie as HTTP-only
                .secure(false)   // Use in production; set to false for local testing without HTTPS
                .path("/")      // Cookie available to the entire application
                .maxAge(7 * 24 * 60 * 60) // Expires in 7 days
                .sameSite("Strict") // Prevent CSRF attacks
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new AuthDTO.ResponseNoMFA("User logged in successfully", token));
    }

    @GetMapping()
    public String hello(){
        return "hello";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@Valid @RequestBody AuthDTO.RegisterRequest registerRequest) throws Exception {
        String qrCode = authService.registerUser(registerRequest);
        if (qrCode.startsWith("data")) {
            return ResponseEntity.ok(new AuthDTO.ResponseMFA("MFA QR code generated", null, qrCode));
        }
        else {
            return ResponseEntity.ok(new AuthDTO.ResponseNoMFA("User registered successfully", qrCode));
        }
    }

    @PostMapping("/validate-mfa")
    public ResponseEntity<?> validateMfa(@Valid @RequestBody AuthDTO.MfaRequest mfaRequest) throws Exception {
        Authentication authentication;
        User user = temporaryUserStore.getUser(mfaRequest.username());

        // Case 1: Handle registration flow
        if (user != null) {
            boolean isValidCode = mfaService.validateCode(user.getMfaSecret(), String.valueOf(mfaRequest.code()));

            if (!isValidCode) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthDTO.ResponseNoMFA("Invalid MFA code", null));
            }

            user.setRole(Role.USER);
            userRepository.save(user);
            temporaryUserStore.removeUser(mfaRequest.username());

            authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    null,
                    user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // Case 2: Handle login flow
        else {
            user = userRepository.findByUsername(mfaRequest.username()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthDTO.ResponseNoMFA("User not found", null));
            }
            boolean isValidCode = mfaService.validateCode(user.getMfaSecret(), String.valueOf(mfaRequest.code()));
            if (!isValidCode) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthDTO.ResponseNoMFA("Invalid MFA code", null));
            }

            // Re-authenticate the user manually after MFA validation
            authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    null,
                    user.getAuthorities() // Set user's roles/authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Generate token and return success response
        String token = authService.generateToken(authentication);
        return ResponseEntity.ok(new AuthDTO.ResponseNoMFA("User successfully authenticated", token));
    }


}