package com.project_managament.servlets;

import com.project_managament.models.Board;
import com.project_managament.models.User;
import com.project_managament.repositories.impl.BoardRepositoryImpl;
import com.project_managament.services.BoardService;
import com.project_managament.services.impl.BoardServiceImpl;
import com.project_managament.utils.AuthUtil;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebServlet({"/boards/add", "/boards/update", "/boards/delete", "/boards/user"})
public class BoardServlet extends HttpServlet {
    private final BoardService boardService = new BoardServiceImpl(new BoardRepositoryImpl());

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
            User user = AuthUtil.getLoggedInUser(req);
            if (user == null) throw new IllegalArgumentException("Unauthorized");

            String uri = req.getRequestURI();
            if (uri.contains("add")) handleAdd(req, res, user);
            else if (uri.contains("update")) handleUpdate(req, res, user);
            else if (uri.contains("delete")) handleDelete(req, res, user);
            else if (uri.contains("user")) handleGetBoardsByUser(req, res, user);
            else throw new IllegalArgumentException("Invalid request");
        } catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse res, User user) throws IOException {
        Map<String, String> data = JsonUtils.parseJsonRequest(req);
        Board board = Board.builder()
                .title(data.get("title"))
                .description(data.get("description"))
                .createdAt(LocalDateTime.now())
                .ownerId(user.getId())
                .build();
        int success = boardService.addBoard(board);
        JsonUtils.sendJsonResponse(res, success != -1 ? 200 : 400, success != -1 ? "Board created successfully!" : "Failed to create board!");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse res, User user) throws IOException {
        Map<String, String> data = JsonUtils.parseJsonRequest(req);
        int id = Integer.parseInt(data.get("id"));
        Board board = boardService.getBoard(id).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        if (board.getOwnerId() != user.getId()) throw new IllegalArgumentException("Unauthorized");

        board.setTitle(data.get("title"));
        board.setDescription(data.get("description"));
        boolean success = boardService.updateBoard(board);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Board updated successfully!" : "Failed to update board!");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res, User user) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Board board = boardService.getBoard(id).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        if (board.getOwnerId() != user.getId()) throw new IllegalArgumentException("Unauthorized");

        boolean success = boardService.deleteBoard(id);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Board deleted successfully!" : "Failed to delete board!");
    }

    private void handleGetBoardsByUser(HttpServletRequest req, HttpServletResponse res, User user) throws IOException {
        List<Board> boards = boardService.getBoardsByUser(user.getId());
        JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, boards);
    }
}
