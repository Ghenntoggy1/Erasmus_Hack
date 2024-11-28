package org.example.Security.MFA;

import org.example.Auth.AuthDTO;
import org.example.Auth.AuthService;
import org.example.User.User;
import org.example.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mfa")
public class MFAController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MFAService mfaService;
    @Autowired
    private AuthService authService;




}
