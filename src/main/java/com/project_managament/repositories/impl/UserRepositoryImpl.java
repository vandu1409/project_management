package com.project_managament.repositories.impl;

import com.project_managament.models.*;
import com.project_managament.models.enums.TokenType;
import com.project_managament.repositories.*;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private static final String INSERT_SQL = "INSERT INTO users (email, password, created_at, deleted_at, is_active) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE users SET email=?, password=?, deleted_at=?, is_active=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM users WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM users";
    private static final String SELECT_BY_EMAIL_SQL = "SELECT * FROM users WHERE email=?";
    private static final String CHECK_EMAIL_EXIST_SQL = "SELECT COUNT(*) FROM users WHERE email = ?";
    private static final String GET_USER_BY_VALID_TOKEN =
            "SELECT u.* FROM users u " +
                    "JOIN tokens t ON u.id = t.user_id " +
                    "WHERE t.token = ? AND t.type = ? AND t.expires_at > NOW()";

    @Override
    public int insert(User user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setTimestamp(3, user.getCreatedAt() != null ? Timestamp.valueOf(user.getCreatedAt()) : null);
            ps.setTimestamp(4, user.getDeletedAt() != null ? Timestamp.valueOf(user.getDeletedAt()) : null);
            ps.setBoolean(5, user.getIsActive());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Lấy ID tự sinh
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public boolean update(User user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setTimestamp(3, user.getDeletedAt() != null ? Timestamp.valueOf(user.getDeletedAt()) : null);
            ps.setBoolean(4, user.getIsActive());
            ps.setInt(5, user.getId());
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
    public User getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findByEmail(String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL_SQL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_EMAIL_EXIST_SQL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu COUNT(*) > 0 thì email đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public User getUserByValidToken(String token, TokenType type) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_VALID_TOKEN)) {
            stmt.setString(1, token);
            stmt.setString(2, type.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private User mapToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                rs.getBoolean("is_active")
        );
    }


}
