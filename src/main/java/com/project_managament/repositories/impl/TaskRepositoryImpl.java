package com.project_managament.repositories.impl;

import com.project_managament.models.Task;
import com.project_managament.repositories.TaskRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {
    private static final String INSERT_SQL = "INSERT INTO tasks (title, description, status, created_at, updated_at, deleted_at, expired_at, task_list_id, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE tasks SET title=?, description=?, status=?, updated_at=?, deleted_at=?, expired_at=?, position=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM tasks WHERE id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM tasks WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM tasks";
    private static final String MOVE_SQL = "UPDATE tasks SET task_list_id=?, position=?, updated_at=? WHERE id=?";
    private static final String FIND_BY_TASK_LIST_ID_SQL =
            "SELECT t.* FROM task_list tl " +
                    "INNER JOIN task t ON tl.id = t.task_list_id " +
                    "WHERE tl.id = ? " +
                    "ORDER BY t.position ASC";

    @Override
    public List<Task> findByTaskListId(int taskListId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_TASK_LIST_ID_SQL)) {

            ps.setInt(1, taskListId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = mapToTask(rs);
                tasks.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tasks by task list id", e);
        }

        return tasks;
    }




    @Override
    public int insert(Task task) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setTaskParameters(ps, task);
            ps.setInt(9, task.getPosition());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting task", e);
        }
        return -1;
    }

    @Override
    public boolean update(Task task) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            setTaskParameters(ps, task);
            ps.setInt(7, task.getPosition());
            ps.setInt(8, task.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task", e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task", e);
        }
    }

    @Override
    public Task getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToTask(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching task by ID", e);
        }
        return null;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                tasks.add(mapToTask(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all tasks", e);
        }
        return tasks;
    }

    public boolean moveTask(int taskId, long newTaskListId, int newPosition) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(MOVE_SQL)) {
            ps.setLong(1, newTaskListId);
            ps.setInt(2, newPosition);
            ps.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setInt(4, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error moving task", e);
        }
    }

    private void setTaskParameters(PreparedStatement ps, Task task) throws SQLException {
        ps.setString(1, task.getTitle());
        ps.setString(2, task.getDescription());
        ps.setString(3, task.getStatus());
        ps.setTimestamp(4, task.getCreatedAt() != null ? Timestamp.valueOf(task.getCreatedAt()) : null);
        ps.setTimestamp(5, task.getUpdatedAt() != null ? Timestamp.valueOf(task.getUpdatedAt()) : null);
        ps.setTimestamp(6, task.getDeletedAt() != null ? Timestamp.valueOf(task.getDeletedAt()) : null);
        ps.setTimestamp(7, task.getExpiredAt() != null ? Timestamp.valueOf(task.getExpiredAt()) : null);
        ps.setInt(8, task.getTaskListId());
    }

    private Task mapToTask(ResultSet rs) throws SQLException {
        return new Task(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getInt("position"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                rs.getTimestamp("expired_at") != null ? rs.getTimestamp("expired_at").toLocalDateTime() : null,
                rs.getInt("task_list_id")
        );
    }


    private static final String FIND_BY_TASK_LIST_ID_SQL =
            "SELECT t.* FROM task_list tl " +
                    "INNER JOIN task t ON tl.id = t.task_list_id " +
                    "WHERE tl.id = ? " +
                    "ORDER BY t.position ASC";

    @Override
    public List<Task> findByTaskListId(int taskListId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_TASK_LIST_ID_SQL)) {

            ps.setInt(1, taskListId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = mapToTask(rs);
                tasks.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tasks by task list id", e);
        }

        return tasks;
    }

}