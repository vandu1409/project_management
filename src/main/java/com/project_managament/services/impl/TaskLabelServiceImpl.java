package com.project_managament.services.impl;

import com.project_managament.models.TaskLabel;
import com.project_managament.repositories.TaskLabelRepository;
import com.project_managament.services.TaskLabelService;
import com.project_managament.utils.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class TaskLabelServiceImpl implements TaskLabelService {
    private final TaskLabelRepository taskLabelRepository;

    public TaskLabelServiceImpl(TaskLabelRepository taskLabelRepository) {
        this.taskLabelRepository = taskLabelRepository;
    }

    @Override
    public int addTaskLabel(TaskLabel taskLabel) {
        validateTaskLabel(taskLabel);
        return taskLabelRepository.insert(taskLabel);
    }

    @Override
    public boolean updateTaskLabel(TaskLabel taskLabel) {
        validateTaskLabel(taskLabel);
        return taskLabelRepository.update(taskLabel);
    }

    @Override
    public boolean deleteTaskLabel(int id) {
        ValidationUtil.requirePositive(id, "Invalid task label ID!");
        return taskLabelRepository.delete(id);
    }

    @Override
    public Optional<TaskLabel> getTaskLabelById(int id) {
        ValidationUtil.requirePositive(id, "Invalid task label ID!");
        return Optional.ofNullable(taskLabelRepository.getById(id));
    }

    @Override
    public List<TaskLabel> getAllTaskLabels() {
        return taskLabelRepository.getAll();
    }

    @Override
    public List<TaskLabel> getTaskLabelsByTaskId(int taskId) {
        ValidationUtil.requirePositive(taskId, "Invalid task ID!");
        return taskLabelRepository.getByTaskId(taskId);
    }

    private void validateTaskLabel(TaskLabel taskLabel) {
        ValidationUtil.requireNonNull(taskLabel, "Task label cannot be null!");
        ValidationUtil.requireNonEmpty(taskLabel.getName(), "Task label name is required!");
        ValidationUtil.validateColor(taskLabel.getColor(), "Invalid color format! Use HEX (e.g., #FF5733).");
    }
}
