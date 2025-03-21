package com.project_managament.repositories;

import com.project_managament.models.BoardMember;
import com.project_managament.repositories.common.BaseRepository;

import java.util.Optional;

public interface BoardMemberRepository extends BaseRepository<BoardMember> {
  Optional<BoardMember> findByBoardAndUser(int boardId, int userId);
    boolean updateStatus(int boardId, int userId, String status);
}
