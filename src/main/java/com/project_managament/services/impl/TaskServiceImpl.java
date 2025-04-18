package com.project_managament.services.impl;

import com.project_managament.models.Task;
import com.project_managament.repositories.TaskRepository;
import com.project_managament.services.TaskService;

import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public List<Task> findByTaskListId(int taskListId) {
        return taskRepository.findByTaskListId(taskListId);
    }

    @Override
    public int addTask(Task task) {
        validateTask(task);
        return taskRepository.insert(task);
    }

    @Override
    public boolean updateTask(Task task) {
        validateTask(task);
        return taskRepository.update(task);
    }

    @Override
    public boolean deleteTask(int id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid task ID");
        return taskRepository.delete(id);
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("Invalid task ID");
        return Optional.ofNullable(taskRepository.getById(id));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.getAll();
    }



    private void validateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (task.getTaskListId() == null || task.getTaskListId() <= 0) {
            throw new IllegalArgumentException("Invalid task list ID");
        }
        if (task.getPosition() < 0) {
            throw new IllegalArgumentException("Task position cannot be negative");
        }
    }
}
