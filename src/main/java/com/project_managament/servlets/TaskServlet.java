package com.project_managament.servlets;

import com.project_managament.models.Task;
import com.project_managament.repositories.impl.TaskRepositoryImpl;
import com.project_managament.services.TaskService;
import com.project_managament.services.impl.TaskServiceImpl;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebServlet({"/tasks/*"})
public class TaskServlet extends HttpServlet {
    private final TaskService taskService = new TaskServiceImpl(new TaskRepositoryImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handleRequest(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String uri = req.getRequestURI();
            if (uri.contains("details")) handleDetails(req,resp);
        }catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonUtils.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String uri = req.getRequestURI();
            Map<String, String> data = JsonUtils.parseJsonRequest(req);

            if (uri.contains("add")) handleAdd(data, res);
            else if (uri.contains("update")) handleUpdate(data, res);
            else if (uri.contains("delete")) handleDelete(req, res);
            else if (uri.contains("move")) handleMove(data, res);
            else throw new IllegalArgumentException("Invalid request");

        } catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleAdd(Map<String, String> data, HttpServletResponse res) throws IOException {
        Task task = Task.builder()
                .title(data.get("title"))
                .description(data.get("description"))
                .status(data.get("status"))
                .position(Integer.parseInt(data.get("position")))
                .taskListId(Integer.parseInt( data.get("taskListId")))
                .createdAt(LocalDateTime.now())
                .build();

        int taskId = taskService.addTask(task);
        JsonUtils.sendJsonResponse(res, taskId != -1 ? 200 : 400, taskId != -1 ? "Task added successfully!" : "Failed to add task!");
    }

    private void handleUpdate(Map<String, String> data, HttpServletResponse res) throws IOException {
        Task task = taskService.getTaskById(Integer.parseInt(data.get("id")))
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setTitle(data.get("title"));
        task.setDescription(data.get("description"));
        task.setStatus(data.get("status"));
        task.setUpdatedAt(LocalDateTime.now());

        boolean success = taskService.updateTask(task);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Task updated successfully!" : "Failed to update task!");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean success = taskService.deleteTask(id);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Task deleted successfully!" : "Failed to delete task!");
    }

    private void handleMove(Map<String, String> data, HttpServletResponse res) throws IOException {
        Task task = taskService.getTaskById(Integer.parseInt(data.get("id")))
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setPosition(Integer.parseInt(data.get("position")));
        task.setTaskListId(Integer.parseInt( data.get("taskListId")));
        task.setUpdatedAt(LocalDateTime.now());

        boolean success = taskService.updateTask(task);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Task moved successfully!" : "Failed to move task!");
    }

    private void handleDetails(HttpServletRequest request,HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("taskListId"));
        List<Task> tasks = taskService.findByTaskListId(id);
        JsonUtils.sendJsonResponse(response,  200, tasks);
    }
}
