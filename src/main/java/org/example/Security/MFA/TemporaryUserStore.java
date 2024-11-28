package org.example.Security.MFA;

import org.example.User.User;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TemporaryUserStore {
    private final ConcurrentHashMap<String, User> tempUserStore = new ConcurrentHashMap<>();

    public void addUser(String username, User user) {
        tempUserStore.put(username, user);
    }

    public User getUser(String username) {
        return tempUserStore.get(username);
    }

    public void removeUser(String username) {
        tempUserStore.remove(username);
    }
}
