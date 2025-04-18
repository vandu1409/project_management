package com.project_managament.servlets;

import com.project_managament.dtos.BoardMemberDTO;
import com.project_managament.mapper.BoardMemberMapper;
import com.project_managament.models.BoardMember;
import com.project_managament.models.User;
import com.project_managament.models.UserProfile;
import com.project_managament.repositories.impl.BoardMemberRepositoryImpl;
import com.project_managament.repositories.impl.UserProfileRepositoryImpl;
import com.project_managament.repositories.impl.UserRepositoryImpl;
import com.project_managament.services.BoardMemberService;
import com.project_managament.services.UserProfileService;
import com.project_managament.services.UserService;
import com.project_managament.services.impl.BoardMemberServiceImpl;
import com.project_managament.services.impl.UserProfileServiceImpl;
import com.project_managament.services.impl.UserServiceImpl;
import com.project_managament.utils.AuthUtil;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet({"/board-members/*"})
public class BoardMemberServlet extends HttpServlet {
    private final BoardMemberService boardMemberService = new BoardMemberServiceImpl(new BoardMemberRepositoryImpl());
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());
    private final UserProfileService userProfileService = new UserProfileServiceImpl(new UserProfileRepositoryImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handleRequest(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.contains("process")) handleProcess(req, resp);
        else if (uri.contains("details")) getBoardMemberByBoard(req, resp);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            User user = AuthUtil.getLoggedInUser(req);
            if (user == null) throw new IllegalArgumentException("Unauthorized");

            String uri = req.getRequestURI();
            if (uri.contains("invite")) handleInvite(req, res);
            else if (uri.contains("approve")) handleApprove(req, res, user);
            else throw new IllegalArgumentException("Invalid request");
        } catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleInvite(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        int boardId = Integer.parseInt(req.getParameter("boardId"));

        System.err.println(email);
        System.err.println(boardId);

        boolean success = boardMemberService.inviteMember(email, boardId);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400,
                success ? "Invitation sent successfully!" : "Failed to send invitation.");
    }


    private void handleApprove(HttpServletRequest req, HttpServletResponse res, User user) throws IOException {

        int boardMemberId = Integer.parseInt(req.getParameter("boardMemberId"));

        boolean success = boardMemberService.approveMember(boardMemberId, user.getId());
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Member approved successfully!" : "Failed to approve member.");
    }

    private void handleProcess(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String message;
        String title;
        try {
            String inviteCode = req.getParameter("inviteCode");
            String token = req.getParameter("token");

            boardMemberService.processInvitation(inviteCode, token);

            title = "Tham gia bảng thành công!";
            message = "Yêu cầu của bạn đã được gửi đi. Vui lòng chờ quản trị viên phê duyệt để bắt đầu cộng tác trên board.";
        } catch (IllegalArgumentException e) {
            title = "Lời mời đã hết hạn hoặc đã được sử dụng";
            message = "Liên kết bạn nhận được không còn hiệu lực. Vui lòng liên hệ người quản lý board để được gửi lại lời mời mới.";
        }

        req.setAttribute("message", message);
        req.setAttribute("title", title);
        req.getRequestDispatcher("/views/notification.jsp").forward(req, res);

    }

    private void getBoardMemberByBoard(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        int boardId = Integer.parseInt(req.getParameter("boardId"));

        List<BoardMember> boardMembers = boardMemberService.getBoardMembersByBoardId(boardId);

        List<BoardMemberDTO> boardMemberDTOS = boardMembers.stream()
                .map(b -> {
                    User currentUser = userService.getUserById(b.getUserId()).orElse(null);
                    UserProfile userProfile = userProfileService.getUserProfileById(currentUser.getId()).orElse(null);

                    return BoardMemberMapper.toBoardMemberDTO(b, currentUser, userProfile);
                }).collect(Collectors.toList());

        JsonUtils.sendJsonResponse(res, 200, boardMemberDTOS);
    }
}
