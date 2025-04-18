package com.project_managament.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskList {
    private int id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private int boardId;
    private List<Task> tasks;


}
