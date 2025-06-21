package com.example.noticeboard.token.repository;

import com.example.noticeboard.token.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(String name, String refreshToken);

    Optional<RefreshToken> findByTokenAndUserId(String requestRefreshToken, String username);

    void deleteById(String userId);
}
