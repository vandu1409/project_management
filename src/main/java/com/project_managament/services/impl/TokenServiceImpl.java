package com.project_managament.services.impl;

import com.project_managament.models.Token;
import com.project_managament.models.enums.TokenType;
import com.project_managament.repositories.TokenRepository;
import com.project_managament.services.TokenService;

import java.util.Optional;

public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public boolean insertToken(Token token) {
        return tokenRepository.insertToken(token);
    }

    @Override
    public Optional<Token> getValidToken(String token,TokenType type) {
        return Optional.of(tokenRepository.getValidToken(token,type));
    }

    @Override
    public boolean deleteToken(String token) {
        return tokenRepository.deleteToken(token);
    }

    @Override
    public Optional<Token> getTokenByUserAndType(int userId, TokenType type) {
        return Optional.of(tokenRepository.getTokenByUserAndType(userId,type));
    }

    @Override
    public String generateActivationToken(int userId, TokenType type){
        return tokenRepository.generateActivationToken(userId,type);
    }


}
