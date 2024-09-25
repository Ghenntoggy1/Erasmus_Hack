package org.example.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.User.Role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "name")
    private String firstName;
    @Column(name = "surname")
    private String lastName;
    @Column(name = "username",unique = true)
    private String username;
    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "password")
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "phone")
    private String phone;
    @Column(name = "enabled")
    private boolean enabled;




}