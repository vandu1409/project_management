package com.project_managament.repositories.impl;

import com.project_managament.models.Token;
import com.project_managament.models.enums.TokenType;
import com.project_managament.repositories.TokenRepository;
import com.project_managament.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class TokenRepositoryImpl implements TokenRepository {
    private static final String INSERT_TOKEN =
            "INSERT INTO tokens (user_id, token, type, expires_at, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_VALID_TOKEN =
            "SELECT * FROM tokens WHERE token = ? AND type = ? AND expires_at > NOW()";
    private static final String DELETE_TOKEN =
            "DELETE FROM tokens WHERE token = ?";
    private static final String GET_TOKEN_BY_USER_AND_TYPE =
            "SELECT * FROM tokens WHERE user_id = ? AND type = ? ORDER BY created_at DESC LIMIT 1";




    @Override
    public boolean insertToken(Token token) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_TOKEN)) {
            stmt.setInt(1, token.getUserId());
            stmt.setString(2, token.getToken());
            stmt.setString(3, token.getType().toString());
            stmt.setTimestamp(4, Timestamp.valueOf(token.getExpiresAt()));
            stmt.setTimestamp(5, Timestamp.valueOf(token.getCreatedAt()));

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Token getValidToken(String token, TokenType type) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_VALID_TOKEN)) {
            stmt.setString(1, token);
            stmt.setString(2, type.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToToken(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteToken(String token) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_TOKEN)) {
            stmt.setString(1, token);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Token getTokenByUserAndType(int userId, TokenType type) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_TOKEN_BY_USER_AND_TYPE)) {
            stmt.setInt(1, userId);
            stmt.setString(2, type.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToToken(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private Token mapToToken(ResultSet rs) throws SQLException {
        return new Token(
                rs.getInt("id"),
                rs.getInt("user_id"),
                TokenType.valueOf(rs.getString("type")),
                rs.getString("token"),
                rs.getTimestamp("expires_at").toLocalDateTime(),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public String generateActivationToken(int userId, TokenType type) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        Token newToken = new Token(0,userId, type, token, expiresAt, LocalDateTime.now());
        insertToken(newToken);

        return token;
    }
}
