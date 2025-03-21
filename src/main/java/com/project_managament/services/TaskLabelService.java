package com.project_managament.services;

import com.project_managament.models.TaskLabel;

import java.util.List;
import java.util.Optional;

public interface TaskLabelService {
    int addTaskLabel(TaskLabel taskLabel);
    boolean updateTaskLabel(TaskLabel taskLabel);
    boolean deleteTaskLabel(int id);
    Optional<TaskLabel> getTaskLabelById(int id);
    List<TaskLabel> getAllTaskLabels();
    List<TaskLabel> getTaskLabelsByTaskId(int taskId);
}