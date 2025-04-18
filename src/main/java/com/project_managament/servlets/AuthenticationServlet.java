package com.project_managament.servlets;

import com.project_managament.repositories.impl.UserRepositoryImpl;
import com.project_managament.services.UserService;
import com.project_managament.services.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet({"/verify-account"})
public class AuthenticationServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.contains("verify-account")) {
            String token = req.getParameter("token");

            if (token == null || token.isBlank()) {
                String error = URLEncoder.encode("Thiếu token xác thực.", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/?errorMessage=" + error);
                return;
            }

            try {
                userService.verifyAccount(token);
                String message = URLEncoder.encode("Xác thực tài khoản thành công!", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/?message=" + message);
            } catch (RuntimeException ex) {
                String rawMessage = ex.getMessage() != null ? ex.getMessage() : "Đã xảy ra lỗi không xác định.";
                String error = URLEncoder.encode(rawMessage, StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/?errorMessage=" + error);
            }
        }
    }

}
