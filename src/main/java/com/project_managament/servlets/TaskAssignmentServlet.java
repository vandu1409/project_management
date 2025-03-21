package com.project_managament.servlets;

import com.project_managament.models.TaskAssignment;
import com.project_managament.services.TaskAssignmentService;
import com.project_managament.services.impl.TaskAssignmentServiceImpl;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet({"/task-assignment/add", "/task-assignment/update", "/task-assignment/delete", "/task-assignment/get", "/task-assignment/all"})
public class TaskAssignmentServlet extends HttpServlet {
    private final TaskAssignmentService taskAssignmentService = new TaskAssignmentServiceImpl(null); // Inject repo vào constructor

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
        TaskAssignment taskAssignment = TaskAssignment.builder()
                .taskId(Integer.parseInt(data.get("taskId")))
                .userId(Integer.parseInt(data.get("userId")))
                .build();

        int id = taskAssignmentService.addTaskAssignment(taskAssignment);
        JsonUtils.sendJsonResponse(res, id != -1 ? 200 : 400, id != -1 ? "Task assigned successfully!" : "Failed to assign task!");
    }

    private void handleUpdate(Map<String, String> data, HttpServletResponse res) throws IOException {
        TaskAssignment taskAssignment = TaskAssignment.builder()
                .id(Integer.parseInt(data.get("id")))
                .build();

        boolean success = taskAssignmentService.updateTaskAssignment(taskAssignment);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Task updated successfully!" : "Failed to update task!");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean success = taskAssignmentService.deleteTaskAssignment(id);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Task deleted successfully!" : "Failed to delete task!");
    }

    private void handleGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<TaskAssignment> taskAssignment = taskAssignmentService.getTaskAssignmentById(id);
        if (taskAssignment.isPresent()) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, taskAssignment.get());
        } else {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_NOT_FOUND, "Task assignment not found!");
        }
    }

    private void handleGetAll(HttpServletResponse res) throws IOException {
        JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, taskAssignmentService.getAllTaskAssignments());
    }
}
