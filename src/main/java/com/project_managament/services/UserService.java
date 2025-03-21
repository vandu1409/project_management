package com.project_managament.services;

import com.project_managament.models.User;
import com.project_managament.models.enums.TokenType;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void verifyAccount(String token);
    int addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    Optional<User> getUserById(int id);
    List<User> getAllUsers();
    boolean registerUser(User user);
    Optional<User> loginUser(String email, String password);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByValidToken(String token, TokenType type);
}
