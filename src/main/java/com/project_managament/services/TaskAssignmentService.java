package com.project_managament.services;

import com.project_managament.models.TaskAssignment;

import java.util.List;
import java.util.Optional;

public interface TaskAssignmentService {
    int addTaskAssignment(TaskAssignment taskAssignment);
    boolean deleteTaskAssignment(int id);
    boolean updateTaskAssignment(TaskAssignment taskAssignment);
    Optional<TaskAssignment> getTaskAssignmentById(int id);
    List<TaskAssignment> getAllTaskAssignments();
}
