package com.project_managament.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project_managament.models.Board;
import com.project_managament.models.Task;
import com.project_managament.models.TaskList;
import com.project_managament.models.User;
import com.project_managament.repositories.impl.BoardRepositoryImpl;
import com.project_managament.repositories.impl.TaskListRepositoryImpl;
import com.project_managament.repositories.impl.TaskRepositoryImpl;
import com.project_managament.services.BoardService;
import com.project_managament.services.TaskListService;
import com.project_managament.services.TaskService;
import com.project_managament.services.impl.BoardServiceImpl;
import com.project_managament.services.impl.TaskListServiceImpl;
import com.project_managament.services.impl.TaskServiceImpl;
import com.project_managament.utils.AuthUtil;

import com.project_managament.utils.JsonUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;


@WebServlet({"/dashboard/*"})

public class DashboardServlet extends HttpServlet {
    private final BoardService boardService = new BoardServiceImpl(new BoardRepositoryImpl());
    private final TaskListService taskListService = new TaskListServiceImpl(new TaskListRepositoryImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handleRequest(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!AuthUtil.isAuthenticated(request)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/index.jsp");
            dispatcher.forward(request, response);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/dashboard.jsp");
        dispatcher.forward(request, response);

    }


    private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            User user = AuthUtil.getLoggedInUser(req);

            if (user == null) throw new IllegalArgumentException("Unauthorized");

            String path = req.getPathInfo(); // eg: /add

            if (path == null || path.equals("/")) {

                throw new IllegalArgumentException("No action provided");

            } else if (path.equals("/add")) {
                handleAdd(req, res, user);

            } else if (path.equals("/update")) {
                handleUpdate(req, res, user);

            } else if (path.equals("/delete")) {
                handleDelete(req, res, user);

            } else if (path.equals("/get_boards")) {
                handleGetBoardsByUser(req, res, user);

            } else if (path.equals("/board_id")) {
                String boardIdParam = req.getParameter("board_id");
                if (boardIdParam == null || boardIdParam.isEmpty()) {

                    throw new IllegalArgumentException("Missing 'board_id' parameter");
                }
                int boardId = Integer.parseInt(boardIdParam);
                Optional<Board> boardOptional = boardService.getBoard(boardId);
                Board board = boardOptional.orElseThrow(() -> new IllegalArgumentException("Board not found"));
                getTasksByBoardId(req, res, user, board);

            } else if (path.equals("/add_task_list")) {
                addTaskList(req, res);

            } else if (path.equals("/remove_task_list")) {
                removeTaskList(req, res);

            } else {
                throw new IllegalArgumentException("Invalid request");

            }

        } catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse res, User user) throws IOException, ServletException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        Board board = Board.builder()
                .title(title)
                .description(description)
                .createdAt(LocalDateTime.now())
                .ownerId(user.getId())
                .build();

        long boardId = boardService.addBoard(board);

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();

        if (boardId != -1) {
            out.write("{\"status\":\"success\", \"boardId\":" + boardId + "}");
        } else {
            out.write("{\"status\":\"fail\"}");
        }

        out.flush();
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
        try {
            System.out.println(req.getParameter("board_id"));

            int id = Integer.parseInt(req.getParameter("board_id"));
            Board board = boardService.getBoard(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy "));
            if (board.getOwnerId() != user.getId()) {
                throw new IllegalArgumentException("Unauthorized");
            }
            boolean success = boardService.deleteBoard(id);
            JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Xóa Thành Công!" : "Có Lỗi Khi Xóa, vui lòng thử lại");

        } catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(res, 400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.sendJsonResponse(res, 500, "Something went wrong!");
        }

    }

    private void handleGetBoardsByUser(HttpServletRequest req, HttpServletResponse res, User user) throws IOException {
        List<Board> boards = boardService.getBoardsByUser(user.getId());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("boards", boards);
        JsonUtils.sendJsonResponse(res, 200, responseData);
    }

    private void getTasksByBoardId(HttpServletRequest req, HttpServletResponse res, User user, Board board) throws IOException, ServletException {
        try {
            int boardId = Integer.parseInt(req.getParameter("board_id"));

            if (board != null) {
                List<TaskList> tasks = taskListService.getTasksByBoardId(boardId);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("task_list", tasks);
                responseData.put("selected_board", board);
                JsonUtils.sendJsonResponse(res, 200, responseData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.sendJsonResponse(res, 500, "Lỗi khi ghi JSON: " + e.getMessage());

        }
    }

    private void addTaskList(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String title = req.getParameter("title");
        int boardId = Integer.parseInt(req.getParameter("board_id"));

        TaskList taskList = TaskList.builder()
                .title(title)
                .createdAt(LocalDateTime.now())
                .boardId(boardId)
                .build();
        boolean success = (taskListService.addTaskList(taskList) != -1);
        // Set JSON response
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        out.write("{\"status\":\"" + (success ? "success" : "fail") + "\"}");
        out.flush();
    }

    private void removeTaskList(HttpServletRequest req, HttpServletResponse res) throws IOException {

        int taskListId = Integer.parseInt(req.getParameter("task_list_id"));

        taskListService.getTaskListById(taskListId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy "));

        boolean success = taskListService.deleteTaskList(taskListId);
        // Set JSON response
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Xóa Thành Công!" : "Có Lỗi Khi Xóa, vui lòng thử lại");

    }

}