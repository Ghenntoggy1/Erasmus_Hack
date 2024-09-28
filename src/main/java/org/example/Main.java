package org.example;

import org.example.Security.RsaKeyConfigProperties;
import org.example.User.Role.Role;
import org.example.User.User;
import org.example.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@SpringBootApplication()
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
    }
//    @Bean
//    public CommandLineRunner initializeUser(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
//        return args -> {
//
//            User user = new User();
//            user.setUsername("admins");
//            user.setEmail("admins@gmail.com");
//            user.setPassword(passwordEncoder.encode("admins"));
//            user.setEnabled(true);
//            user.setRole(Role.valueOf("admin"));
//            user.setPhone("123456789");
//
//            // Save the user to the database
//            userRepository.save(user);
//
//            User user2 = new User();
//            user2.setUsername("user");
//            user2.setEmail("user@gmail.com");
//            user2.setPassword(passwordEncoder.encode("users"));
//            user2.setEnabled(true);
//            user2.setRole(Role.valueOf("user"));
//            user2.setPhone("123456789");
//
//            // Save the user to the database
//            userRepository.save(user2);
//
//        };
//    }
}