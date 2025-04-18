package com.project_managament.repositories.impl;

import com.project_managament.models.BoardMember;
import com.project_managament.repositories.BoardMemberRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardMemberRepositoryImpl implements BoardMemberRepository {
    private static final String INSERT_SQL = "INSERT INTO board_members (board_id, user_id, joined_at, role, status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE board_members SET role=?, status=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM board_members WHERE id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM board_members WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM board_members";
    private static final String SELECT_BY_BOARD_AND_USER_SQL =
            "SELECT * FROM board_members WHERE board_id = ? AND user_id = ? LIMIT 1";
    private static final String UPDATE_STATUS_SQL = "UPDATE board_members SET status = ? WHERE board_id = ? AND user_id = ?";
    private static final String SELECT_BOARD_MEMBERS_BY_BOARD_ID = "SELECT bm.* FROM board_members bm WHERE bm.board_id = ?";


    @Override
    public List<BoardMember> getBoardMembersByBoardId(int boardId) {
        List<BoardMember> boardMembers = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BOARD_MEMBERS_BY_BOARD_ID)) {
            stmt.setInt(1, boardId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BoardMember boardMember = mapToBoardMember(rs);
                boardMembers.add(boardMember);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardMembers;
    }

    @Override
    public int insert(BoardMember boardMember) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, boardMember.getBoardId());
            ps.setInt(2, boardMember.getUserId());
            ps.setTimestamp(3, boardMember.getJoinedAt() != null ? Timestamp.valueOf(boardMember.getJoinedAt()) : null);
            ps.setString(4, boardMember.getRole());
            ps.setString(5, boardMember.getStatus());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public boolean update(BoardMember boardMember) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, boardMember.getRole());
            ps.setString(2, boardMember.getStatus());
            ps.setInt(3, boardMember.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BoardMember getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToBoardMember(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BoardMember> getAll() {
        List<BoardMember> boardMembers = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                boardMembers.add(mapToBoardMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardMembers;
    }

    @Override
    public Optional<BoardMember> findByBoardAndUser(int boardId, int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_BOARD_AND_USER_SQL)) {
            ps.setInt(1, boardId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToBoardMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean updateStatus(int boardId, int userId, String status) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS_SQL)) {
            ps.setString(1, status);
            ps.setInt(2, boardId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    private BoardMember mapToBoardMember(ResultSet rs) throws SQLException {
        return new BoardMember(
                rs.getInt("id"),
                rs.getInt("board_id"),
                rs.getInt("user_id"),
                rs.getTimestamp("joined_at") != null ? rs.getTimestamp("joined_at").toLocalDateTime() : null,
                rs.getString("role"),
                rs.getString("status")
        );
    }

}
