package com.example.noticeboard.common;

import com.example.noticeboard.common.exception.CustomBadRequestException;
import com.example.noticeboard.common.redis.RedisService;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
@Component
public class JwtTokenProvider {

    private final RedisService redisService;
    private final String secretKey;
    private final Key key;


    private final long accessTokenValidity = 1000 * 60 * 30;
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,RedisService redisService) {
        this.secretKey = secretKey;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.redisService = redisService;
    }

    public String createAccessToken(String userId) {
        return createToken(userId, accessTokenValidity);
    }

    public String createRefreshToken(String userId) {
        return createToken(userId, refreshTokenValidity);
    }

    private String createToken(String userId, long validity) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdAndIsValid(String token) throws CustomBadRequestException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new CustomBadRequestException("토큰이 유효하지 않습니다.");
        }
    }

    @SneakyThrows
    public boolean isBlackList(String token) {
        if (redisService.getBlackList("blackList:%s".formatted(getUserIdAndIsValid(token))) !=null) {
            throw new RuntimeException("로그아웃된 토큰입니다");
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}