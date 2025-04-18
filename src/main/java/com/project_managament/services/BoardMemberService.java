package com.project_managament.services;

import com.project_managament.models.Board;
import com.project_managament.models.BoardMember;

import java.util.List;
import java.util.Optional;

public interface BoardMemberService {
    List<BoardMember> getBoardMembersByBoardId(int boardId);

    boolean inviteMember(String email, int boardId);

    boolean approveMember(int boardMemberId, int ownerId);

    boolean processInvitation(String inviteCode, String token);

    int addBoardMember(BoardMember boardMember);
    boolean deleteBoardMember(int id);
    boolean updateBoardMember(BoardMember boardMember);
    List<BoardMember> getBoardMembers();
    Optional<BoardMember> getBoardMember(int id);
}
