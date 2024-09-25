package org.example;

import org.example.Security.RsaKeyConfigProperties;
import org.example.User.User;
import org.example.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@SpringBootApplication()
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    public CommandLineRunner initializeUser(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {

            User user = new User();
            user.setUsername("exampleuser");
            user.setEmail("example@gmail.com");
            user.setPassword(passwordEncoder.encode("examplepassword"));

            // Save the user to the database
            userRepository.save(user);

        };
    }
}