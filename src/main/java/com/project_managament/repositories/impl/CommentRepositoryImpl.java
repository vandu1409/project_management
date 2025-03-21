package com.project_managament.repositories.impl;

import com.project_managament.models.Comment;
import com.project_managament.repositories.CommentRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {

    private static final String INSERT_SQL = "INSERT INTO comments (content, created_at, deleted_at, task_id, user_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE comments SET content=?, deleted_at=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM comments WHERE id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM comments WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM comments";

    @Override
    public int insert(Comment comment) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, comment.getContent());
            ps.setTimestamp(2, Timestamp.valueOf(comment.getCreatedAt()));
            ps.setTimestamp(3, comment.getDeletedAt() != null ? Timestamp.valueOf(comment.getDeletedAt()) : null);
            ps.setInt(4, comment.getTaskId());
            ps.setInt(5, comment.getUserId());


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
    public boolean update(Comment comment) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, comment.getContent());
            ps.setTimestamp(2, comment.getDeletedAt() != null ? Timestamp.valueOf(comment.getDeletedAt()) : null);
            ps.setInt(3, comment.getId());
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
    public Comment getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToComment(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Comment> getAll() {
        List<Comment> comments = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                comments.add(mapToComment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    private Comment mapToComment(ResultSet rs) throws SQLException {
        return new Comment(
                rs.getInt("id"),
                rs.getString("content"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                rs.getInt("task_id"),
                rs.getInt("user_id")
        );
    }
}

