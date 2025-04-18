package com.project_managament.dtos;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BoardMemberDTO {
    private int id;
    private int boardId;
    private String role;
    private String status;
    private String fullName;
    private String email;
    private String avatar;

}
