package com.project_managament.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    private int id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private int taskId;
    private int userId;
}
