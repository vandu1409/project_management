package com.project_managament.services.impl;

import com.project_managament.models.Board;
import com.project_managament.models.BoardMember;
import com.project_managament.models.User;
import com.project_managament.models.enums.TokenType;
import com.project_managament.repositories.BoardMemberRepository;
import com.project_managament.repositories.impl.BoardRepositoryImpl;
import com.project_managament.repositories.impl.TokenRepositoryImpl;
import com.project_managament.repositories.impl.UserRepositoryImpl;
import com.project_managament.services.*;
import com.project_managament.utils.EmailTemplateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BoardMemberServiceImpl implements BoardMemberService {

    private final BoardMemberRepository boardMemberRepository;

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    private final EmailService emailService = new EmailServiceImpl();

    private final TokenService tokenService = new TokenServiceImpl(new TokenRepositoryImpl());

    private final BoardService boardService = new BoardServiceImpl(new BoardRepositoryImpl());

    public BoardMemberServiceImpl(BoardMemberRepository boardMemberRepository) {
        this.boardMemberRepository = boardMemberRepository;
    }

    @Override
    public boolean inviteMember(String email, int boardId) {

        Board board = boardService.getBoard(boardId).orElseThrow(
                () -> new IllegalArgumentException("Invalid id!")
        );

        User user = userService.getUserByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("User with this email is not registered!")
        );

        BoardMember boardMember = boardMemberRepository.findByBoardAndUser(board.getId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User is already a member of this board!"));

        String token = tokenService.generateActivationToken(user.getId(), TokenType.ACCOUNT_INVITATION);
        String inviteLink = "https://your-app.com/board-member/" + board.getInviteCode() + "?token=" + token;

        String htmlContent = EmailTemplateUtil.loadTemplate(
                "board-invitation-email.html"
                , "{{userName}}", user.getEmail()
                , "{{boardTitle}}", board.getTitle()
                , "{{inviteLink}}", inviteLink
        );
        emailService.sendEmail(user.getEmail(), "Thư mời tham gia dự án", htmlContent);

        return true;
    }

    @Override
    public boolean approveMember(int boardMemberId, int ownerId) {
        BoardMember boardMember = getBoardMember(boardMemberId).orElseThrow(
                () -> new IllegalArgumentException("Board member not found!")
        );


        Optional<BoardMember> ownerOpt = boardMemberRepository.findByBoardAndUser(boardMember.getBoardId(), ownerId);
        if (ownerOpt.isEmpty() || !"OWNER".equals(ownerOpt.get().getRole())) {
            throw new IllegalArgumentException("Only the board owner can approve members!");
        }

        boolean approved = boardMemberRepository.updateStatus(boardMember.getBoardId(), boardMember.getUserId(), "ACTIVE");

        if (approved) {
            User user = userService.getUserById(boardMember.getUserId()).orElseThrow(
                    () -> new IllegalArgumentException("User not found!")
            );
            String htmlContent = EmailTemplateUtil.loadTemplate(
                    "invitation-success-email.html"

            );
            emailService.sendEmail(user.getEmail(), "Thư mời tham gia dự án", htmlContent);
        }

        return approved;
    }

    @Override
    public boolean processInvitation(String inviteCode, String token) {

        User user = userService.getUserByValidToken(token, TokenType.ACCOUNT_INVITATION)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid or expired invitation token!")
                );

        Board board = boardService.getByInviteCode(inviteCode)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid invite code!")
                );

        BoardMember newMember = BoardMember.builder()
                .boardId(board.getId())
                .userId(user.getId())
                .role("MEMBER")
                .status("PENDING")
                .joinedAt(LocalDateTime.now())
                .build();

        addBoardMember(newMember);
        tokenService.deleteToken(token);

        return true;
    }


    @Override
    public int addBoardMember(BoardMember boardMember) {
        return boardMemberRepository.insert(boardMember);
    }

    @Override
    public boolean deleteBoardMember(int id) {
        return boardMemberRepository.delete(id);
    }

    @Override
    public boolean updateBoardMember(BoardMember boardMember) {
        return boardMemberRepository.update(boardMember);
    }

    @Override
    public List<BoardMember> getBoardMembers() {
        return boardMemberRepository.getAll();
    }

    @Override
    public Optional<BoardMember> getBoardMember(int id) {
        return Optional.of(boardMemberRepository.getById(id));
    }
}
