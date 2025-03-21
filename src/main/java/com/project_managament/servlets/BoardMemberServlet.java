package com.project_managament.servlets;

import com.project_managament.models.User;
import com.project_managament.repositories.impl.BoardMemberRepositoryImpl;
import com.project_managament.services.BoardMemberService;
import com.project_managament.services.impl.BoardMemberServiceImpl;
import com.project_managament.utils.AuthUtil;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet({"/board-members/invite", "/board-members/approve", "/board-members/process"})
public class BoardMemberServlet extends HttpServlet {
    private final BoardMemberService boardMemberService = new BoardMemberServiceImpl(new BoardMemberRepositoryImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handleRequest(req, res);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            User user = AuthUtil.getLoggedInUser(req);
            if (user == null) throw new IllegalArgumentException("Unauthorized");

            String uri = req.getRequestURI();
            if (uri.contains("invite")) handleInvite(req, res);
            else if (uri.contains("approve")) handleApprove(req, res, user);
            else if (uri.contains("process")) handleProcess(req, res);
            else throw new IllegalArgumentException("Invalid request");
        } catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleInvite(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, String> data = JsonUtils.parseJsonRequest(req);
        String email = data.get("email");
        int boardId = Integer.parseInt(data.get("boardId"));

        boolean success = boardMemberService.inviteMember(email, boardId);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Invitation sent successfully!" : "Failed to send invitation.");
    }

    private void handleApprove(HttpServletRequest req, HttpServletResponse res, User user) throws IOException {
        Map<String, String> data = JsonUtils.parseJsonRequest(req);
        int boardMemberId = Integer.parseInt(data.get("boardMemberId"));

        boolean success = boardMemberService.approveMember(boardMemberId, user.getId());
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Member approved successfully!" : "Failed to approve member.");
    }

    private void handleProcess(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, String> data = JsonUtils.parseJsonRequest(req);
        String inviteCode = data.get("inviteCode");
        String token = data.get("token");

        boolean success = boardMemberService.processInvitation(inviteCode, token);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "User successfully joined the board!" : "Failed to process invitation.");
    }
}
