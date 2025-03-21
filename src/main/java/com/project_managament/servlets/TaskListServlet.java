package com.project_managament.servlets;

import com.project_managament.models.TaskList;
import com.project_managament.repositories.impl.TaskListRepositoryImpl;
import com.project_managament.services.TaskListService;
import com.project_managament.services.impl.TaskListServiceImpl;
import com.project_managament.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@WebServlet("/task-lists/*")
public class TaskListServlet extends HttpServlet {
    private final TaskListService taskListService = new TaskListServiceImpl(new TaskListRepositoryImpl());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> jsonData = JsonUtils.parseJsonRequest(request);
        TaskList taskList = TaskList.builder()
                .title(jsonData.get("title"))
                .boardId(Integer.parseInt(jsonData.get("boardId")))
                .createdAt(LocalDateTime.now())
                .build();

        JsonUtils.sendJsonResponse(response, HttpServletResponse.SC_OK,
                taskListService.addTaskList(taskList) > 0 ? "TaskList created!" : "Failed to create TaskList.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        JsonUtils.sendJsonResponse(response, HttpServletResponse.SC_OK,
                (pathInfo == null || pathInfo.equals("/"))
                        ? taskListService.getAllTaskList()
                        : taskListService.getTaskListById(Integer.parseInt(pathInfo.substring(1))).orElse(null));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> jsonData = JsonUtils.parseJsonRequest(request);
        TaskList taskList = TaskList.builder()
                .id(Integer.parseInt(jsonData.get("id")))
                .title(jsonData.get("title"))
                .build();

        JsonUtils.sendJsonResponse(response, HttpServletResponse.SC_OK,
                taskListService.updateTaskList(taskList) ? "TaskList updated!" : "Failed to update TaskList.");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int taskListId = Integer.parseInt(request.getPathInfo().substring(1));
        TaskList taskList = taskListService.getTaskListById(taskListId).orElse(null);

        if (taskList == null) {
            JsonUtils.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, "TaskList not found.");
            return;
        }

        taskList.setDeletedAt(LocalDateTime.now());
        JsonUtils.sendJsonResponse(response, HttpServletResponse.SC_OK,
                taskListService.updateTaskList(taskList) ? "TaskList deleted!" : "Failed to delete TaskList.");
    }
}
