package com.project_managament.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {
    private int id;
    private String title;
    private String description;
    private String status;
    private int position;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime updatedAt;
    private Long taskListId;
}
