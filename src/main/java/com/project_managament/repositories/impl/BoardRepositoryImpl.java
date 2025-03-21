package com.project_managament.repositories.impl;

import com.project_managament.models.Board;
import com.project_managament.repositories.BoardRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoardRepositoryImpl implements BoardRepository {
    private static final String INSERT_SQL = "INSERT INTO boards (title, description, created_at, deleted_at, owner_id, invite_code) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE boards SET title=?, description=?, deleted_at=?, owner_id=?, invite_code=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM boards WHERE id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM boards WHERE id=?";
    private static final String SELECT_BY_INVITE_CODE_SQL = "SELECT * FROM boards WHERE invite_code=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM boards";
    private static final String SELECT_BY_USER = "SELECT b.* FROM boards b " +
            "JOIN board_members bm ON b.id = bm.board_id " +
            "WHERE bm.user_id = ?";
    @Override
    public int insert(Board board) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getDescription());
            ps.setTimestamp(3, board.getCreatedAt() != null ? Timestamp.valueOf(board.getCreatedAt()) : null);
            ps.setTimestamp(4, board.getDeletedAt() != null ? Timestamp.valueOf(board.getDeletedAt()) : null);
            ps.setInt(5, board.getOwnerId());
            ps.setString(6, generateUniqueInviteCode());

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
    public boolean update(Board board) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getDescription());
            ps.setTimestamp(3, board.getDeletedAt() != null ? Timestamp.valueOf(board.getDeletedAt()) : null);
            ps.setInt(4, board.getOwnerId());
            ps.setString(5, board.getInviteCode());
            ps.setInt(6, board.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Board getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToBoard(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Board getByInviteCode(String inviteCode) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_INVITE_CODE_SQL)) {
            ps.setString(1, inviteCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToBoard(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Board> getAll() {
        List<Board> boards = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                boards.add(mapToBoard(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    @Override
    public List<Board> getBoardsByUser(int userId) {
        List<Board> boards = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USER)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Board board = mapToBoard(rs);
                boards.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }


    private Board mapToBoard(ResultSet rs) throws SQLException {
        return new Board(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                rs.getInt("owner_id"),
                rs.getString("invite_code")
        );
    }

    private String generateUniqueInviteCode() {
        String inviteCode;
        do {
            inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        } while (getByInviteCode(inviteCode) != null);
        return inviteCode;
    }
}