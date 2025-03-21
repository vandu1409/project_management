package com.project_managament.repositories;

import com.project_managament.models.Token;
import com.project_managament.models.enums.TokenType;
import com.project_managament.repositories.common.BaseRepository;

public interface TokenRepository {
    boolean insertToken(Token token);

    Token getValidToken(String token, TokenType type);

    boolean deleteToken(String token);

    Token getTokenByUserAndType(int userId, TokenType type);

    String generateActivationToken(int userId, TokenType type);
}
