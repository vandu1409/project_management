package com.project_managament.servlets;

import com.project_managament.models.User;
import com.project_managament.models.UserProfile;
import com.project_managament.repositories.impl.UserProfileRepositoryImpl;
import com.project_managament.services.UserProfileService;
import com.project_managament.services.impl.UserProfileServiceImpl;
import com.project_managament.utils.AuthUtil;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@WebServlet({"/user/add", "/user/update", "/user/delete", "/user/get", "/user/all"})
@MultipartConfig
public class UserProfileServlet extends HttpServlet {
    private final UserProfileService userProfileService = new UserProfileServiceImpl(new UserProfileRepositoryImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String uri = req.getRequestURI();
        try {
            if (uri.contains("add")) {
                handleAdd(req, res);
            } else if (uri.contains("update")) {
                handleUpdate(req, res);
            } else {
                JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, "Invalid POST request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String uri = req.getRequestURI();
        try {
            if (uri.contains("delete")) {
                handleDelete(req, res);
            } else if (uri.contains("get")) {
                handleGet(req, res);
            } else if (uri.contains("all")) {
                handleGetAll(res);
            } else {
                JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        String avatarPath = saveAvatarToUploads(req, req.getPart("avatar"));

        User user = AuthUtil.getLoggedInUser(req);
        if (user == null) {
            JsonUtils.sendJsonResponse(res, 401, "Chưa đăng nhập");
            return;
        }

        UserProfile userProfile = UserProfile.builder()
                .userId(user.getId())
                .fullname(fullname)
                .phone(phone)
                .avatar(avatarPath)
                .build();

        int id = userProfileService.addUserProfile(userProfile);
        JsonUtils.sendJsonResponse(res, id != -1 ? 200 : 400, id != -1 ? "User profile added!" : "Add failed!");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        User user = AuthUtil.getLoggedInUser(req);
        if (user == null) {
            JsonUtils.sendJsonResponse(res, 401, "Chưa đăng nhập");
            return;
        }

        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        Part avatarPart = req.getPart("avatar");

        Optional<UserProfile> existing = userProfileService.getUserProfileById(user.getId());

        String avatarPath = null;
        if (avatarPart != null && avatarPart.getSize() > 0) {
            avatarPath = saveAvatarToUploads(req, avatarPart);
        }

        UserProfile profile = UserProfile.builder()
                .userId(user.getId())
                .fullname(fullname)
                .phone(phone)
                .avatar(avatarPath != null ? avatarPath : existing.map(UserProfile::getAvatar).orElse(null))
                .build();

        boolean success;
        if (existing.isPresent()) {
            profile.setId(existing.get().getId());
            success = userProfileService.updateUserProfile(profile);
        } else {
            profile.setId(0);
            success = userProfileService.addUserProfile(profile) != -1;
        }

        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Cập nhật thành công!" : "Cập nhật thất bại!");
    }

    private String saveAvatarToUploads(HttpServletRequest req, Part avatarPart) throws IOException {
        if (avatarPart == null || avatarPart.getSize() == 0) return null;

        String fileName = UUID.randomUUID() + "_" + Paths.get(avatarPart.getSubmittedFileName()).getFileName();
        String uploadFolder = req.getServletContext().getRealPath("/uploads/avatars");

        File uploadDir = new File(uploadFolder);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        Path filePath = Paths.get(uploadFolder, fileName);
        try (InputStream input = avatarPart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return req.getContextPath() + "/uploads/avatars/" + fileName;
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean success = userProfileService.deleteUserProfile(id);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Deleted!" : "Delete failed!");
    }

    private void handleGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = AuthUtil.getLoggedInUser(req);
        if (user == null) {
            JsonUtils.sendJsonResponse(res, 401, "Chưa đăng nhập");
            return;
        }

        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(user.getId());
        JsonUtils.sendJsonResponse(res, 200, userProfile.orElse(null));
    }

    private void handleGetAll(HttpServletResponse res) throws IOException {
        JsonUtils.sendJsonResponse(res, 200, userProfileService.getAllUserProfiles());
    }
}
