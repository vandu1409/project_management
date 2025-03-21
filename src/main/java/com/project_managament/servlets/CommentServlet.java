package com.project_managament.servlets;

import com.project_managament.models.Comment;
import com.project_managament.services.CommentService;
import com.project_managament.services.impl.CommentServiceImpl;
import com.project_managament.utils.JsonUtils;
import com.project_managament.utils.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet({"/comment/add", "/comment/update", "/comment/delete", "/comment/get", "/comment/all"})
public class CommentServlet extends HttpServlet {
    private final CommentService commentService = new CommentServiceImpl(null); // Inject repo

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handleRequest(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handleRequest(req, res);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try{
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
        Comment comment = Comment.builder()
                .content(data.get("content"))
                .userId(Integer.parseInt(data.get("userId")))
                .taskId(Integer.parseInt(data.get("taskId")))
                .build();

        int id = commentService.addComment(comment);
        JsonUtils.sendJsonResponse(res, id != -1 ? 200 : 400, id != -1 ? "Comment added successfully!" : "Failed to add comment!");
    }

    private void handleUpdate(Map<String, String> data, HttpServletResponse res) throws IOException {
        Comment comment = Comment.builder()
                .id(Integer.parseInt(data.get("id")))
                .content(data.get("content"))
                .userId(Integer.parseInt(data.get("userId")))
                .taskId(Integer.parseInt(data.get("taskId")))
                .build();

        boolean success = commentService.updateComment(comment);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Comment updated successfully!" : "Failed to update comment!");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean success = commentService.deleteComment(id);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Comment deleted successfully!" : "Failed to delete comment!");
    }

    private void handleGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<Comment> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, comment.get());
        } else {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_NOT_FOUND, "Comment not found!");
        }
    }

    private void handleGetAll(HttpServletResponse res) throws IOException {
        JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, commentService.getAllComments());
    }
}
