package com.project_managament.services;

import com.project_managament.models.Token;
import com.project_managament.models.enums.TokenType;

import java.util.Optional;

public interface TokenService {
    boolean insertToken(Token token);
    Optional<Token> getValidToken(String token,TokenType type);
    boolean deleteToken(String token);
    Optional<Token> getTokenByUserAndType(int userId, TokenType type);

    String generateActivationToken(int userId, TokenType type);
}
