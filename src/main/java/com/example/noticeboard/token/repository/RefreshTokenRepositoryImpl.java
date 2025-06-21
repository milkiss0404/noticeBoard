package com.example.noticeboard.token.repository;

import com.example.noticeboard.token.entity.RefreshToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(String name, String refreshToken) {
        Instant now = Instant.now();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .userId(name)
                .token(refreshToken)
                .expiryDate(now.plus(Duration.ofHours(24))).build();
        em.persist(refreshTokenEntity);
    }

    @Override
    public Optional<RefreshToken> findByTokenAndUserId(String token, String username) {
        return jpaRefreshTokenRepository.findByTokenAndUserId(token, username);

    }

    @Override
    public void deleteById(String userId) {
        jpaRefreshTokenRepository.deleteByUserId(userId);
    }
}
