package com.project_managament.utils;

import com.project_managament.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AuthUtil {
    private static final String AUTH_SESSION_KEY = "loggedInUser";

    public static void setLoggedInUser(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute(AUTH_SESSION_KEY, user);
    }

    public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(AUTH_SESSION_KEY);
        }
        return null;
    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        return getLoggedInUser(request) != null;
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
