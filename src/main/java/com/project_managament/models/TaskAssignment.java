package com.project_managament.models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskAssignment {
    private int id;
    private int taskId;
    private int userId;
}
