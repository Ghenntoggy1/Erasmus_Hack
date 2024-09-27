package org.example.Auth;

import org.example.Security.MFA.MFAService;
import org.example.Security.MFA.TemporaryUserStore;
import org.example.User.Role.Role;
import org.example.User.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.example.User.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private MFAService mfaService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemporaryUserStore temporaryUserStore;

    public String registerUser(AuthDTO.RegisterRequest registerRequest) {
        // Check if user already exists (only check in permanent storage)
        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        String hashedPassword = passwordEncoder.encode(registerRequest.password());
        User newUser = new User();
        newUser.setFirstName(registerRequest.firstName());
        newUser.setLastName(registerRequest.lastName());
        newUser.setUsername(registerRequest.username());
        newUser.setEmail(registerRequest.email());
        newUser.setPassword(hashedPassword);
        newUser.setPhone(registerRequest.phone());
        String qrCode = null;
        if (registerRequest.mfaEnabled()) {
            String secret = mfaService.createSecret();
            newUser.setMfaEnabled(true);
            newUser.setMfaSecret(secret);
            qrCode = mfaService.generateQRCode(secret, registerRequest.username());
            temporaryUserStore.addUser(registerRequest.username(), newUser);
        }
        else {
            Authentication authentication;
            newUser.setMfaEnabled(false);
            newUser.setRole(Role.user);
            userRepository.save(newUser);
            // Programmatically authenticate the user
            authentication = new UsernamePasswordAuthenticationToken(
                    newUser.getUsername(),
                    null, // You can use null for credentials as they are not needed anymore
                    newUser.getAuthorities() // Grant user their roles/authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Return success response with token
            return generateToken(authentication);
        }
        return qrCode;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }



}