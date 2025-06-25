package com.example.noticeboard.common.handler;

import com.example.noticeboard.common.JwtTokenProvider;
import com.example.noticeboard.common.redis.RedisService;
import com.example.noticeboard.token.repository.JpaRefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class LogoutHandlerImpl implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final RedisService redisService;

    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtTokenProvider.resolveToken(request);


        if (token != null && jwtTokenProvider.isTokenValid(token)) {
            String userId = jwtTokenProvider.getUserId(token);

            String blackListKey = "blackList:%s".formatted(userId);
            redisService.setBlackList(blackListKey,token, 30L);

            String key = "refreshToken:userId %s".formatted(userId);
            redisService.deleteData(key);
//            jpaRefreshTokenRepository.deleteByUserId((userId));
        }
    }
}
