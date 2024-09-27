package org.example.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Security.RsaKeyConfigProperties;
import org.example.Security.RsaUtil;
import org.example.User.Role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "name")
    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Column(name = "surname")
    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Column(name = "username", unique = true)
    @NotNull(message = "Username cannot be null")
    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters")
    private String username;

    @Column(name = "email", unique = true)
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password")
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role cannot be null")
    private Role role;

    @Column(name = "phone")
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is not valid")
    private String phone;

    @Column(name = "enabled")
    private boolean enabled;


    @Column(name = "mfa_enabled")
    private boolean mfaEnabled;

    @Autowired
    @Transient
    private RsaKeyConfigProperties rsaKeyConfig;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    // Set the encrypted MFA secret
    public void setMfaSecret(String mfaSecret) {
        try {
            this.mfaSecret = RsaUtil.encrypt(mfaSecret, rsaKeyConfig.publicKey());
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt MFA secret", e);
        }
    }

    // Retrieve the decrypted MFA secret
    public String getMfaSecret() {
        try {
            return RsaUtil.decrypt(this.mfaSecret, rsaKeyConfig.privateKey());
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt MFA secret", e);
        }
    }

        @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // If you have multiple roles or authorities, you can adjust this.
        // For a single role, it could be as simple as:
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // You can customize this if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Customize if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Customize if needed
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;  // Based on your "enabled" field
    }

}
