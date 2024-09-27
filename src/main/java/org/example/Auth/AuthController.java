package org.example.Auth;

import org.example.Security.MFA.MFAService;
import org.example.Security.MFA.TemporaryUserStore;
import org.example.User.Role.Role;
import org.example.User.User;
import org.example.User.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TemporaryUserStore temporaryUserStore;

    @Autowired
    private MFAService mfaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest userLogin) throws IllegalAccessException {
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                userLogin.username(),
                                userLogin.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthUser userDetails = (AuthUser) authentication.getPrincipal();
        User user = userRepository.findByUsername(userLogin.username()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isMfaEnabled()) {
            // MFA is enabled, request MFA code
            return ResponseEntity.ok(new AuthDTO.ResponseNoMFA("Enter MFA code", null)); // Indicate to enter MFA code
        }

        log.info("Token requested for user :{}", authentication.getAuthorities());
        String token = authService.generateToken(authentication);

        AuthDTO.ResponseNoMFA response = new AuthDTO.ResponseNoMFA("User logged in successfully", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody AuthDTO.RegisterRequest registerRequest) {
        String qrCode = authService.registerUser(registerRequest);
        if (qrCode.startsWith("data")) {
            return ResponseEntity.ok(new AuthDTO.ResponseMFA("MFA QR code generated", null, qrCode));
        }
        else {
            return ResponseEntity.ok(new AuthDTO.ResponseNoMFA("User registered successfully", qrCode));
        }
    }

    @PostMapping("/validate-mfa")
    public ResponseEntity<?> validateMfa(@RequestBody AuthDTO.MfaRequest mfaRequest) {
        // Retrieve user from a temporary store for registration or a user repository for login
        Authentication authentication;
        User user = temporaryUserStore.getUser(mfaRequest.username());
        if (user == null) {
            user = userRepository.findByUsername(mfaRequest.username()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthDTO.ResponseNoMFA("User not found", null));
            }
            else{
                boolean isValidCode = mfaService.validateCode(user.getMfaSecret(), mfaRequest.code());
                if (!isValidCode) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthDTO.ResponseNoMFA("Invalid MFA code", null));
                }
                 authentication =  SecurityContextHolder.getContext().getAuthentication();
            }
        }
        else{
            user.setRole(Role.user);
            userRepository.save(user);
            temporaryUserStore.removeUser(mfaRequest.username());

            // Programmatically authenticate the user
             authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    null, // You can use null for credentials as they are not needed anymore
                    user.getAuthorities() // Grant user their roles/authorities
            );

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        String token = authService.generateToken(authentication);

        // Return success response with token
        return ResponseEntity.ok(new AuthDTO.ResponseNoMFA("User successfully authenticated", token));
    }


}