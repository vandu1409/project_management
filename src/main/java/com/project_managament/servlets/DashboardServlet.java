package com.project_managament.servlets;

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
    private final TaskService taskChildService = new TaskServiceImpl(new TaskRepositoryImpl());

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

            } else if (path.equals("/get_task_child")) {
                getTaskChild(req, res);

            } else if (path.equals("/create_task_child")) {
                createTaskChild(req, res);

            } else if (path.equals("/get_detail_task")) {
                getDetailTask(req, res);

            }  else if (path.equals("/update_task")) {
                updateTask(req, res);

            }else if (path.equals("/update_detail")) {
                updateDetail(req, res);

            }  else {
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
            if (board != null) {
                int boardId = Integer.parseInt(req.getParameter("board_id"));

                List<TaskList> taskLists = taskListService.getTasksByBoardId(boardId);

                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("task_list", taskLists);

                responseData.put("selected_board", board);

                JsonUtils.sendJsonResponse(res, 200, responseData);
            } else {
                JsonUtils.sendJsonResponse(res, 400, "Board không hợp lệ hoặc không tồn tại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.sendJsonResponse(res, 500, "Lỗi khi xử lý yêu cầu: " + e.getMessage());
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

    private void createTaskChild(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try{
            String title = req.getParameter("title");
            int taskListId = Integer.parseInt(req.getParameter("task_list_id"));
            String description = req.getParameter("description");

            // To Do Hard code
            String taskStatus = String.valueOf('0');
            int taskPosition = 1;
            Task task = Task.builder()
                    .title(title)
                    .description(description)
                    .status(taskStatus)
                    .position(taskPosition)
                    .taskListId(taskListId)
                    .createdAt(LocalDateTime.now())
                    .build();
            boolean success = (taskChildService.addTask(task) != -1);
            // Set JSON response
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            PrintWriter out = res.getWriter();
            out.write("{\"status\":\"" + (success ? "success" : "fail") + "\"}");
            out.flush();
        }catch (Exception e){
            System.out.println("Task Child:" + e.getMessage());
        }

    }

    private void getTaskChild(HttpServletRequest req, HttpServletResponse res) throws IOException {

            int taskListId = Integer.parseInt(req.getParameter("task_list_id"));

        try{

            // To Do Hard code
            List<Task> tasks = taskChildService.findByTaskListId(taskListId);

            // Set JSON response

            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("task_childs", tasks);
            JsonUtils.sendJsonResponse(res, 200, responseData);
        }catch (Exception e){
            System.out.println("Task Child:" + e.getMessage());
        }

    }

    private void getDetailTask(HttpServletRequest req, HttpServletResponse res) throws IOException {

            int taskId = Integer.parseInt(req.getParameter("task_id"));

        try{

            Optional<Task> task = taskChildService.getTaskById(taskId);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("task_detail", task);

            JsonUtils.sendJsonResponse(res, 200, responseData);
        }catch (Exception e){
            System.out.println("Task Child:" + e.getMessage());
        }

    }

    private void updateTask(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int taskId = Integer.parseInt(req.getParameter("task_child_id"));
        int taskListId = Integer.parseInt(req.getParameter("tas_list_id"));
        boolean success = taskListService.updateTaskChild(taskId, taskListId);

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");


        if (success) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("{\"message\": \"Cập nhật thành công\"}");
        } else {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"message\": \"Cập nhật thất bại\"}");
        }
    }
    private void updateDetail(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            int taskId = Integer.parseInt(req.getParameter("task_id"));
            String description = req.getParameter("description");

            // Lấy task cũ từ DB
            Optional<Task> oldTask = taskChildService.getTaskById(taskId);
            if (oldTask.isEmpty()) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                res.getWriter().write("{\"message\": \"Không tìm thấy task\"}");
                return;
            }

            // Gán dữ liệu mới
            oldTask.get().setDescription(description);
            oldTask.get().setUpdatedAt(LocalDateTime.now());

            boolean success = taskChildService.updateTask(oldTask.orElse(null));

            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");

            if (success) {
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write("{\"message\": \"Cập nhật thành công\"}");
            } else {
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.getWriter().write("{\"message\": \"Cập nhật thất bại\"}");
            }

        } catch (IllegalArgumentException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();

            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("{\"message\": \"Đã xảy ra lỗi không xác định: " + e.getMessage() + "\"}");
        }
    }


}