package com.project_managament.repositories.impl;

import com.project_managament.models.UserProfile;
import com.project_managament.repositories.UserProfileRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserProfileRepositoryImpl implements UserProfileRepository {

    private static final String INSERT_SQL = "INSERT INTO user_profiles (user_id, fullname, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE user_profiles SET fullname=?, phone=? WHERE user_id=?";
    private static final String DELETE_SQL = "DELETE FROM user_profiles WHERE user_id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM user_profiles WHERE user_id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM user_profiles";

    @Override
    public int insert(UserProfile profile) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFullname());
            ps.setString(3, profile.getPhone());

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
    public boolean update(UserProfile profile) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, profile.getFullname());
            ps.setString(2, profile.getPhone());
            ps.setInt(3, profile.getUserId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserProfile getById(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToUserProfile(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserProfile> getAll() {
        List<UserProfile> profiles = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                profiles.add(mapToUserProfile(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profiles;
    }

    private UserProfile mapToUserProfile(ResultSet rs) throws SQLException {
        return new UserProfile(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("fullname"),
                rs.getString("phone")
        );
    }
}
