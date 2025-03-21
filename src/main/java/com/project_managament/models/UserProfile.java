package com.project_managament.models;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfile {
    private int id;
    private int userId;
    private String fullname;
    private String phone;
}
