package com.project_managament.mapper;

import com.project_managament.dtos.BoardMemberDTO;
import com.project_managament.models.BoardMember;
import com.project_managament.models.User;
import com.project_managament.models.UserProfile;

public class BoardMemberMapper {

    public static BoardMemberDTO toBoardMemberDTO(BoardMember boardMember, User user, UserProfile userProfile) {
        return BoardMemberDTO.builder()
                .id(boardMember.getId())
                .status(boardMember.getStatus())
                .boardId(boardMember.getBoardId())
                .role(boardMember.getRole())
                .fullName(userProfile != null && userProfile.getFullname() != null
                        ? userProfile.getFullname()
                        : "Người dùng")
                .avatar(userProfile != null && userProfile.getAvatar() != null
                ? userProfile.getAvatar()
                : null)
                .email(user.getEmail())
                .build();
    }
}
