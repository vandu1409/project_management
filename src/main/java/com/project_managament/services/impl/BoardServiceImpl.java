package com.project_managament.services.impl;

import com.project_managament.models.Board;
import com.project_managament.models.BoardMember;
import com.project_managament.repositories.BoardRepository;
import com.project_managament.repositories.impl.BoardMemberRepositoryImpl;
import com.project_managament.services.BoardMemberService;
import com.project_managament.services.BoardService;
import com.project_managament.utils.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardMemberService boardMemberService = new BoardMemberServiceImpl(new BoardMemberRepositoryImpl());

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    private void validateBoard(Board board, boolean isUpdate) {
        ValidationUtil.requireNonNull(board, "Board cannot be null");
        ValidationUtil.requireNonEmpty(board.getTitle(), "Board title cannot be empty");
        if (isUpdate) {
            ValidationUtil.requirePositive(board.getId(), "Invalid board ID");
        }
    }

    @Override
    public int addBoard(Board board) {
        validateBoard(board, false);
        int boardId = boardRepository.insert(board);

        if (boardId > 0) {

            BoardMember boardMember = new BoardMember(0,boardId,board.getOwnerId(), LocalDateTime.now(),"OWNER","ACTIVE");
            boardMemberService.addBoardMember(boardMember);
        }

        return boardId;
    }


    @Override
    public boolean deleteBoard(int id) {
        ValidationUtil.requirePositive(id, "Invalid board ID");
        return boardRepository.delete(id);
    }

    @Override
    public boolean updateBoard(Board board) {
        validateBoard(board, true);
        return boardRepository.update(board);
    }

    @Override
    public List<Board> getAllBoards() {
        return boardRepository.getAll();
    }

    @Override
    public Optional<Board> getBoard(int id) {
        ValidationUtil.requirePositive(id, "Invalid board ID");
        return Optional.ofNullable(boardRepository.getById(id));
    }

    @Override
    public Optional<Board> getByInviteCode(String code){
        return Optional.ofNullable(boardRepository.getByInviteCode(code));
    }

    @Override
    public List<Board> getBoardsByUser(int userId){
        return boardRepository.getBoardsByUser(userId);
    }
}
