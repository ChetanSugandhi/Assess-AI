package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);
}
