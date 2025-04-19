package com.project_managament.services;

import com.project_managament.models.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findByTaskListId(int taskListId);

    int addTask(Task task);
    boolean updateTask(Task task);
    boolean deleteTask(int id);
    Optional<Task> getTaskById(int id);
    List<Task> getAllTasks();

}
