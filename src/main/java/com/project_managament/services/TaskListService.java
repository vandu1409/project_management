package com.project_managament.services;

import com.project_managament.models.TaskList;

import java.util.List;
import java.util.Optional;

public interface TaskListService {
    int addTaskList(TaskList taskList);
    boolean updateTaskList(TaskList taskList);
    boolean deleteTaskList(int id);
    Optional<TaskList> getTaskListById(int id);
    List<TaskList> getAllTaskList();
}
