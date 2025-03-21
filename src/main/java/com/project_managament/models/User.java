package com.project_managament.models;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    private int id;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Boolean isActive;
}
