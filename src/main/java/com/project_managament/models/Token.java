package com.project_managament.models;

import com.project_managament.models.enums.TokenType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Token {
    private int id;
    private int userId;
    private TokenType type;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
