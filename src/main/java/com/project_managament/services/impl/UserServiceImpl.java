package com.project_managament.services.impl;

import com.project_managament.models.Token;
import com.project_managament.models.User;
import com.project_managament.models.enums.TokenType;
import com.project_managament.repositories.UserRepository;
import com.project_managament.repositories.impl.TokenRepositoryImpl;
import com.project_managament.services.EmailService;
import com.project_managament.services.TokenService;
import com.project_managament.services.UserService;
import com.project_managament.utils.EmailTemplateUtil;
import com.project_managament.utils.PasswordUtil;
import com.project_managament.utils.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService = new EmailServiceImpl();
    private final TokenService tokenService = new TokenServiceImpl(new TokenRepositoryImpl());

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean registerUser(User user) {
        validateUser(user);
        if (userRepository.existsByEmail(user.getEmail())) return false;

        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        int userId = addUser(user);
        if (userId == -1) return false;

        String token = tokenService.generateActivationToken(userId, TokenType.ACCOUNT_ACTIVATION);
        sendActivationEmail(user, "http://localhost:8080/project_managament_war/verify-account?token=" + token);
        return true;
    }

    @Override
    public Optional<User> loginUser(String email, String password) {
   /*     ValidationUtil.validateEmail(email);
        ValidationUtil.validatePassword(password);*/
        return Optional.ofNullable(userRepository.findByEmail(email))
                .filter(user ->
                        PasswordUtil.checkPassword(password, user.getPassword()) &&
                                Boolean.TRUE.equals(user.getIsActive())
                );

    }

    @Override
    public void verifyAccount(String token) {
        Token validToken = tokenService.getValidToken(token, TokenType.ACCOUNT_ACTIVATION)
                .orElseThrow(() -> new IllegalArgumentException("Token không hợp lệ hoặc đã hết hạn."));

        User user = Optional.ofNullable(userRepository.getUserByValidToken(token, TokenType.ACCOUNT_ACTIVATION))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng."));

        user.setIsActive(true);
        if (!updateUser(user)) throw new RuntimeException("Kích hoạt tài khoản thất bại.");
        tokenService.deleteToken(token);
    }

    @Override
    public int addUser(User user) {
        return userRepository.insert(user);
    }

    @Override
    public boolean updateUser(User user) {
        return userRepository.update(user);
    }

    @Override
    public boolean deleteUser(int id) {
        return userRepository.delete(id);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userRepository.getById(id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public Optional<User> getUserByValidToken(String token,TokenType type){
        return Optional.ofNullable(userRepository.getUserByValidToken(token,type));
    }

    private void sendActivationEmail(User user, String activationLink) {
        String htmlContent = EmailTemplateUtil.loadTemplate("activation-email.html", "{{activationLink}}", activationLink);
        emailService.sendEmail(user.getEmail(), "Kích hoạt tài khoản", htmlContent);
    }


    private void validateUser(User user) {
        ValidationUtil.requireNonNull(user, "Người dùng không được để trống!");
        ValidationUtil.validateUser( user.getEmail(), user.getPassword());
    }
}