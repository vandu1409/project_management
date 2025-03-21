package com.project_managament.servlets;

import com.project_managament.models.User;
import com.project_managament.repositories.impl.UserRepositoryImpl;
import com.project_managament.services.UserService;
import com.project_managament.services.impl.UserServiceImpl;
import com.project_managament.utils.AuthUtil;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet({"/register", "/login", "/logout"})
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());
    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handleRequest(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handleRequest(req, res);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String uri = req.getRequestURI();
            if (uri.contains("register")) handleRegister(req, res);
            else if (uri.contains("login")) handleLogin(req, res);
            else if (uri.contains("logout")) handleLogout(req, res);
            else throw new IllegalArgumentException("Invalid request");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Client error: " + e.getMessage(), e);
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Internal server error", e);
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, String> jsonData = JsonUtils.parseJsonRequest(req);
        String email = jsonData.get("email");
        String password = jsonData.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Missing email or password");
        }

        User newUser = new User(0, email, password, LocalDateTime.now(), null, false);
        boolean success = userService.registerUser(newUser);

        if (success) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, "Registration successful! Check your email.");
        } else {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, "Registration failed! Email may already be in use.");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, String> jsonData = JsonUtils.parseJsonRequest(req);
        String email = jsonData.get("email");
        String password = jsonData.get("password");

        Optional<User> user = userService.loginUser(email, password);
        if (user.isPresent()) {
            AuthUtil.setLoggedInUser(req, user.get());
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, "Login successful!");
        } else {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        AuthUtil.logout(req);
        JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, "Logged out successfully!");
    }
}
