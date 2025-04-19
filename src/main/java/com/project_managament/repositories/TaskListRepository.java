package com.project_managament.repositories;

import com.project_managament.models.Task;
import com.project_managament.models.TaskList;
import com.project_managament.repositories.common.BaseRepository;

import java.util.List;

public interface TaskListRepository extends BaseRepository<TaskList> {
    List<TaskList> getTasksByBoardId(int BoardId);
     Boolean updateTaskChild(int taskId,int newTaskListId);

}
