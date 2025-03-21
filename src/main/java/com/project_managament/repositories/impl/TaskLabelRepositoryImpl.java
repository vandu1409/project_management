package com.project_managament.repositories.impl;

import com.project_managament.models.TaskLabel;
import com.project_managament.repositories.TaskLabelRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskLabelRepositoryImpl implements TaskLabelRepository {
    private static final String INSERT_SQL = "INSERT INTO task_labels (name, color, task_id) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE task_labels SET name=?, color=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM task_labels WHERE id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM task_labels WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM task_labels";
    private static final String SELECT_BY_TASK_ID_SQL = "SELECT * FROM task_labels WHERE task_id=?";

    @Override
    public int insert(TaskLabel taskLabel) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, taskLabel.getName());
            ps.setString(2, taskLabel.getColor());
            ps.setInt(3, taskLabel.getTaskId());

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
    public boolean update(TaskLabel taskLabel) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, taskLabel.getName());
            ps.setString(2, taskLabel.getColor());
            ps.setInt(3, taskLabel.getId());

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
    public TaskLabel getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToTaskLabel(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TaskLabel> getAll() {
        List<TaskLabel> taskLabels = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                taskLabels.add(mapToTaskLabel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskLabels;
    }

    @Override
    public List<TaskLabel> getByTaskId(int taskId) {
        List<TaskLabel> taskLabels = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_TASK_ID_SQL)) {
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                taskLabels.add(mapToTaskLabel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskLabels;
    }

    private TaskLabel mapToTaskLabel(ResultSet rs) throws SQLException {
        return new TaskLabel(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("color"),
                rs.getInt("task_id")
        );
    }
}
