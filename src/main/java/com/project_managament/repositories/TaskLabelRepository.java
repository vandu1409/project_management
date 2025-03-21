package com.project_managament.repositories;

import com.project_managament.models.TaskLabel;
import com.project_managament.repositories.common.BaseRepository;

import java.util.List;

public interface TaskLabelRepository extends BaseRepository<TaskLabel> {
    List<TaskLabel> getByTaskId(int id);
}
