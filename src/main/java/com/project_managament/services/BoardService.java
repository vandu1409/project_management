package com.project_managament.services;

import com.project_managament.models.Board;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    int addBoard(Board board);
    boolean deleteBoard(int id);
    boolean updateBoard(Board board);
    List<Board> getAllBoards();
    Optional<Board> getBoard(int id);
    Optional<Board> getByInviteCode(String code);

    List<Board> getBoardsByUser(int userId);
}
