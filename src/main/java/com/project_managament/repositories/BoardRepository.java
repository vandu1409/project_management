package com.project_managament.repositories;

import com.project_managament.models.Board;
import com.project_managament.repositories.common.BaseRepository;

import java.util.List;

public interface BoardRepository extends BaseRepository<Board> {
    Board getByInviteCode(String inviteCode);

    List<Board> getBoardsByUser(int userId);
}
