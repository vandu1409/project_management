package com.project_managament.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BoardMember {
    private int id;
    private int boardId;
    private int userId;
    private LocalDateTime joinedAt;
    private String role;
    private String status;
}
