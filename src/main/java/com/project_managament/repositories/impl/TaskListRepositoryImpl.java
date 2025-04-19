package com.project_managament.repositories.impl;

import com.project_managament.models.Task;
import com.project_managament.models.TaskList;
import com.project_managament.repositories.TaskListRepository;
import com.project_managament.repositories.TaskRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskListRepositoryImpl implements TaskListRepository {
    private static final String INSERT_SQL = "INSERT INTO task_lists (title, created_at, deleted_at, board_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE task_lists SET title=?, deleted_at=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM task_lists WHERE id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM task_lists WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM task_lists";
    /**
     * Task By Board Id
     */
    private static final String TASK_BY_BOARD_ID_SQL = "SELECT * FROM task_lists WHERE board_id=?";
    private static final String UPDATE_TASK_LIST_ID_OF_TASK = "UPDATE tasks SET task_list_id = ? WHERE id = ?";
    private final TaskRepository taskRepository = new TaskRepositoryImpl();

    @Override
    public int insert(TaskList taskList) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, taskList.getTitle());
            ps.setTimestamp(2, Timestamp.valueOf(taskList.getCreatedAt()));
            ps.setTimestamp(3, taskList.getDeletedAt() != null ? Timestamp.valueOf(taskList.getDeletedAt()) : null);
            ps.setInt(4, taskList.getBoardId());

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
    public boolean update(TaskList taskList) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, taskList.getTitle());
            ps.setTimestamp(2, taskList.getDeletedAt() != null ? Timestamp.valueOf(taskList.getDeletedAt()) : null);
            ps.setInt(3, taskList.getId());
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
    public TaskList getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToTaskList(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TaskList> getAll() {
        List<TaskList> taskLists = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                taskLists.add(mapToTaskList(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskLists;
    }
    @Override
    public Boolean updateTaskChild(int taskId,int newTaskListId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_TASK_LIST_ID_OF_TASK)) {
            ps.setInt(1, newTaskListId);
            ps.setInt(2, taskId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private TaskList mapToTaskList(ResultSet rs) throws SQLException {
        TaskList taskList = new TaskList();
        taskList.setId(rs.getInt("id"));
        taskList.setTitle(rs.getString("title"));
        taskList.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        taskList.setDeletedAt(rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null);
        taskList.setBoardId(rs.getInt("board_id"));
        return taskList;
    }

    private final TaskRepository taskListRepository = new TaskRepositoryImpl();


    @Override
    public List<TaskList> getTasksByBoardId(int boardId) {
        if (boardId <= 0)
            throw new IllegalArgumentException("Invalid task boardId");

        List<TaskList> taskLists = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(TASK_BY_BOARD_ID_SQL)) {

            ps.setInt(1, boardId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TaskList taskList = mapToTaskList(rs);

                if (taskRepository != null) {
                    List<Task> taskChildren = taskRepository.findByTaskListId(taskList.getId());
                    taskList.setTasks(taskChildren);
                }

                taskLists.add(taskList);
            }

            return taskLists;

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching task list by board ID", e);
        }
    }

}
