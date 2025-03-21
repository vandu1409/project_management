package com.project_managament.repositories.impl;

import com.project_managament.models.TaskAssignment;
import com.project_managament.repositories.TaskAssignmentRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskAssignmentRepositoryImpl implements TaskAssignmentRepository {

    private static final String INSERT_SQL = "INSERT INTO task_assignments (task_id, user_id) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE task_assignments SET task_id=?, user_id=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM task_assignments WHERE id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM task_assignments WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM task_assignments";

    @Override
    public int insert(TaskAssignment assignment) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setInt(1, assignment.getTaskId());
            ps.setInt(2, assignment.getUserId());

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
    public boolean update(TaskAssignment assignment) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setInt(1, assignment.getTaskId());
            ps.setInt(2, assignment.getUserId());
            ps.setInt(3, assignment.getId());
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
    public TaskAssignment getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToTaskAssignment(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TaskAssignment> getAll() {
        List<TaskAssignment> assignments = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                assignments.add(mapToTaskAssignment(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignments;
    }

    private TaskAssignment mapToTaskAssignment(ResultSet rs) throws SQLException {
        return new TaskAssignment(
                rs.getInt("id"),
                rs.getInt("task_id"),
                rs.getInt("user_id")
        );
    }
}
