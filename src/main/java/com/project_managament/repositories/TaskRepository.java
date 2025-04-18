package com.project_managament.repositories;

import com.project_managament.models.Task;
import com.project_managament.models.TaskList;
import com.project_managament.repositories.common.BaseRepository;

import java.util.List;

public interface TaskRepository extends BaseRepository<Task> {
    List<Task> findByTaskListId(int taskListId);

    @Override
    boolean update(Task task);
}
