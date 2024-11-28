package org.example.User;

import org.example.User.Role.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User getUserbyId(Integer id) {
        return userRepository.findById(Long.valueOf(id)).get();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
}
