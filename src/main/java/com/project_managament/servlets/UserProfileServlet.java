package com.project_managament.servlets;

import com.project_managament.models.UserProfile;
import com.project_managament.services.UserProfileService;
import com.project_managament.services.impl.UserProfileServiceImpl;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet({"/user/add", "/user/update", "/user/delete", "/user/get", "/user/all"})
public class UserProfileServlet extends HttpServlet {
    private final UserProfileService userProfileService = new UserProfileServiceImpl(null); // Inject repository

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
            Map<String, String> data = JsonUtils.parseJsonRequest(req);

            if (uri.contains("add")) handleAdd(data, res);
            else if (uri.contains("update")) handleUpdate(data, res);
            else if (uri.contains("delete")) handleDelete(req, res);
            else if (uri.contains("get")) handleGet(req, res);
            else if (uri.contains("all")) handleGetAll(res);
            else throw new IllegalArgumentException("Invalid request");

        } catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleAdd(Map<String, String> data, HttpServletResponse res) throws IOException {
        UserProfile userProfile = UserProfile.builder()
                .fullname(data.get("fullname"))
                .phone(data.get("phone"))
                .build();

        int id = userProfileService.addUserProfile(userProfile);
        JsonUtils.sendJsonResponse(res, id != -1 ? 200 : 400, id != -1 ? "User profile added successfully!" : "Failed to add user profile!");
    }

    private void handleUpdate(Map<String, String> data, HttpServletResponse res) throws IOException {
        UserProfile userProfile = UserProfile.builder()
                .id(Integer.parseInt(data.get("id")))
                .fullname(data.get("fullname"))
                .phone(data.get("phone"))
                .build();

        boolean success = userProfileService.updateUserProfile(userProfile);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "User profile updated successfully!" : "Failed to update user profile!");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean success = userProfileService.deleteUserProfile(id);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "User profile deleted successfully!" : "Failed to delete user profile!");
    }

    private void handleGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(id);
        if (userProfile.isPresent()) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, userProfile.get());
        } else {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_NOT_FOUND, "User profile not found!");
        }
    }

    private void handleGetAll(HttpServletResponse res) throws IOException {
        JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, userProfileService.getAllUserProfiles());
    }
}
