package com.project_managament.repositories;

import com.project_managament.models.User;
import com.project_managament.models.enums.TokenType;
import com.project_managament.repositories.common.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    User findByEmail(String email);
    boolean existsByEmail(String email);

    User getUserByValidToken(String token, TokenType type);
}
