package com.example.noticeboard.token.repository;

import com.example.noticeboard.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByTokenAndUserId(String token, String username);

    @Modifying
    @Query("delete from RefreshToken rt where rt.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);
}
