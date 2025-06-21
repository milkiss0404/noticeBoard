package com.example.noticeboard.token.service;

import com.example.noticeboard.token.entity.RefreshToken;
import com.example.noticeboard.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(String name, String refreshToken) {
        refreshTokenRepository.save(name, refreshToken);
    }

    public boolean isValid(String requestRefreshToken, String username) {
        return refreshTokenRepository.findByTokenAndUserId(requestRefreshToken, username).isPresent();
    }
}
