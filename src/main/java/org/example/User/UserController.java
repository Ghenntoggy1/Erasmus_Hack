package org.example.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user/hasMFA")
    public ResponseEntity<Boolean> hasMFA(@RequestBody String username) {
        boolean hasMFA = userService.findByUsername(username).isMfaEnabled();
        return ResponseEntity.ok(hasMFA);
    }
}