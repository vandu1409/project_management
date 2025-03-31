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

@WebServlet({"/verify-account"})
public class AuthenticationServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if(uri.contains("verify-account")){
            String token = req.getParameter("token");
            userService.verifyAccount(token);
            req.getRequestDispatcher("/views/indexindex.jsp").forward(req, resp);
        }
    }
}
