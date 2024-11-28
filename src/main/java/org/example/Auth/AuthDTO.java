package org.example.Auth;

import jakarta.validation.constraints.*;

public class AuthDTO {
    public record LoginRequest(
            @NotBlank(message = "Username is required") String username,
            @NotBlank(message = "Password is required") String password) {
    }

    public record ResponseNoMFA(String message, String token) {
    }

    public record ResponseMFA(String message, String token, String qrCode) {
    }

    public record RegisterRequest(
            @NotBlank(message = "First name is required") String firstName,
            @NotBlank(message = "Last name is required") String lastName,
            @NotBlank(message = "Username is required") String username,
            @Email(message = "Email should be valid") String email,
            @NotBlank(message = "Password is required") String password,
            @NotBlank(message = "Phone number is required") String phone,
            boolean enabled,
            boolean mfaEnabled) {
    }

    public record MfaRequest(
            @NotBlank(message = "Username is required") String username,
            @NotNull(message = "MFA code is required") Integer code) {
    }
}
