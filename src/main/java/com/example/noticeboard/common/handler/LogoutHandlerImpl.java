package com.example.noticeboard.common.handler;

import com.example.noticeboard.common.JwtTokenProvider;
import com.example.noticeboard.common.redis.RedisService;
import com.example.noticeboard.token.repository.JpaRefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    @SneakyThrows // 체크 예외(checked exception)를 명시적으로 선언하거나 try-catch 없이 던질 수 있게 해줌
    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtTokenProvider.resolveToken(request);
        String userId = jwtTokenProvider.getUserIdAndIsValid(token);
        if (jwtTokenProvider.isBlackList(token)) {
            response.setStatus(400);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("토큰이 유효하지 않습니다");
        }

        String blackListKey = "blackList:%s".formatted(userId);
        redisService.setBlackList(blackListKey, token, 30L);

        String key = "refreshToken:userId %s".formatted(userId);
        redisService.deleteData(key);
//            jpaRefreshTokenRepository.deleteByUserId((userId));
    }
}

