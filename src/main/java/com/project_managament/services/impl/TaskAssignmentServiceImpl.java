package com.project_managament.services.impl;

import com.project_managament.models.TaskAssignment;
import com.project_managament.repositories.TaskAssignmentRepository;
import com.project_managament.services.TaskAssignmentService;
import com.project_managament.utils.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final TaskAssignmentRepository taskAssignmentRepository;

    public TaskAssignmentServiceImpl(TaskAssignmentRepository taskAssignmentRepository) {
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    @Override
    public int addTaskAssignment(TaskAssignment taskAssignment) {
        ValidationUtil.requireNonNull(taskAssignment, "Task assignment cannot be null!");
        ValidationUtil.requirePositive(taskAssignment.getTaskId(), "Invalid task ID!");
        ValidationUtil.requirePositive(taskAssignment.getUserId(), "Invalid user ID!");


        return taskAssignmentRepository.insert(taskAssignment);
    }

    @Override
    public boolean deleteTaskAssignment(int id) {
        ValidationUtil.requirePositive(id, "Invalid task assignment ID!");
        return taskAssignmentRepository.delete(id);
    }

    @Override
    public boolean updateTaskAssignment(TaskAssignment taskAssignment) {
        ValidationUtil.requireNonNull(taskAssignment, "Task assignment cannot be null!");
        ValidationUtil.requirePositive(taskAssignment.getId(), "Invalid task assignment ID!");

        return taskAssignmentRepository.update(taskAssignment);
    }

    @Override
    public Optional<TaskAssignment> getTaskAssignmentById(int id) {
        ValidationUtil.requirePositive(id, "Invalid task assignment ID!");
        return Optional.ofNullable(taskAssignmentRepository.getById(id));
    }

    @Override
    public List<TaskAssignment> getAllTaskAssignments() {
        return taskAssignmentRepository.getAll();
    }
}
