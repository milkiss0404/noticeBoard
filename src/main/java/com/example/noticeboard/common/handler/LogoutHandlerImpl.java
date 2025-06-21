package com.example.noticeboard.common.handler;

import com.example.noticeboard.common.JwtTokenProvider;
import com.example.noticeboard.token.repository.JpaRefreshTokenRepository;
import com.example.noticeboard.token.repository.RefreshTokenRepository;
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

    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.isTokenValid(token)) {
            String userId = jwtTokenProvider.getUserId(token);
            jpaRefreshTokenRepository.deleteByUserId((userId));
        }


    }
}
