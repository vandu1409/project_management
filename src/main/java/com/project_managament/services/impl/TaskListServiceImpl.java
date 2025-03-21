package com.project_managament.services.impl;

import com.project_managament.models.TaskList;
import com.project_managament.repositories.TaskListRepository;
import com.project_managament.services.TaskListService;
import com.project_managament.utils.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @Override
    public int addTaskList(TaskList taskList) {
        ValidationUtil.requireNonNull(taskList, "TaskList cannot be null");
        ValidationUtil.requireNonEmpty(taskList.getTitle(), "TaskList title cannot be empty");
        ValidationUtil.requirePositive(taskList.getBoardId(), "Invalid board ID");

        return taskListRepository.insert(taskList);
    }

    @Override
    public boolean updateTaskList(TaskList taskList) {
        ValidationUtil.requireNonNull(taskList, "TaskList cannot be null");
        ValidationUtil.requirePositive(taskList.getId(), "Invalid TaskList ID");
        ValidationUtil.requireNonEmpty(taskList.getTitle(), "TaskList title cannot be empty");

        return taskListRepository.update(taskList);
    }

    @Override
    public boolean deleteTaskList(int id) {
        ValidationUtil.requirePositive(id, "Invalid TaskList ID");
        return taskListRepository.delete(id);
    }

    @Override
    public Optional<TaskList> getTaskListById(int id) {
        ValidationUtil.requirePositive(id, "Invalid TaskList ID");
        return Optional.ofNullable(taskListRepository.getById(id));
    }

    @Override
    public List<TaskList> getAllTaskList() {
        return taskListRepository.getAll();
    }
}
