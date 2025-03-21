package com.project_managament.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskLabel {
    private int id;
    private String name;
    private String color;
    private int taskId;
}
