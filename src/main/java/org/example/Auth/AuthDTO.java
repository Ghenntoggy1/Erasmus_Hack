package org.example.Auth;

public class AuthDTO {
    public record LoginRequest(String username, String password) {
    }

    public record ResponseNoMFA(String message, String token) {
    }
    public record ResponseMFA(String message, String token, String qrCode) {
    }
    public record RegisterRequest(
            String firstName,
            String lastName,
            String username,
            String email,
            String password,
            String phone,
            boolean enabled,
            boolean mfaEnabled
    ) {}
    public record MfaRequest(String username, String code){};

}