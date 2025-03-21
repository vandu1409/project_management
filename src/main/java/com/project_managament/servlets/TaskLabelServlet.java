package com.project_managament.servlets;

import com.project_managament.models.TaskLabel;
import com.project_managament.services.TaskLabelService;
import com.project_managament.services.impl.TaskLabelServiceImpl;
import com.project_managament.utils.JsonUtils;
import com.project_managament.utils.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet({"/task-label/add", "/task-label/update", "/task-label/delete", "/task-label/get", "/task-label/all", "/task-label/by-task"})
public class TaskLabelServlet extends HttpServlet {
    private final TaskLabelService taskLabelService = new TaskLabelServiceImpl(null); // Inject repo

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
            else if (uri.contains("by-task")) handleGetByTask(req, res);
            else throw new IllegalArgumentException("Invalid request");

        } catch (IllegalArgumentException e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleAdd(Map<String, String> data, HttpServletResponse res) throws IOException {
        TaskLabel taskLabel = TaskLabel.builder()
                .name(data.get("name"))
                .color(data.get("color"))
                .build();

        int id = taskLabelService.addTaskLabel(taskLabel);
        JsonUtils.sendJsonResponse(res, id != -1 ? 200 : 400, id != -1 ? "Task label added successfully!" : "Failed to add task label!");
    }

    private void handleUpdate(Map<String, String> data, HttpServletResponse res) throws IOException {
        TaskLabel taskLabel = TaskLabel.builder()
                .id(Integer.parseInt(data.get("id")))
                .name(data.get("name"))
                .color(data.get("color"))
                .build();

        boolean success = taskLabelService.updateTaskLabel(taskLabel);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Task label updated successfully!" : "Failed to update task label!");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean success = taskLabelService.deleteTaskLabel(id);
        JsonUtils.sendJsonResponse(res, success ? 200 : 400, success ? "Task label deleted successfully!" : "Failed to delete task label!");
    }

    private void handleGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<TaskLabel> taskLabel = taskLabelService.getTaskLabelById(id);
        if (taskLabel.isPresent()) {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, taskLabel.get());
        } else {
            JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_NOT_FOUND, "Task label not found!");
        }
    }

    private void handleGetAll(HttpServletResponse res) throws IOException {
        JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, taskLabelService.getAllTaskLabels());
    }

    private void handleGetByTask(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int taskId = Integer.parseInt(req.getParameter("taskId"));
        JsonUtils.sendJsonResponse(res, HttpServletResponse.SC_OK, taskLabelService.getTaskLabelsByTaskId(taskId));
    }
}
