package com.project_managament.models;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Board {
    private int id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private int ownerId;
    private String inviteCode;

}
