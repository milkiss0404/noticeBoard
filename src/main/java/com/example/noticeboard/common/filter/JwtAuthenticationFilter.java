package com.example.noticeboard.common.filter;

import com.example.noticeboard.common.JwtTokenProvider;
import com.example.noticeboard.common.exception.CustomBadRequestException;
import com.example.noticeboard.user.application.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/user/join")
                || path.startsWith("/user/login")
                || path.startsWith("/h2-console");
    }

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userDetailService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = getToken(request);
            jwtTokenProvider.isBlackList(token);

            String userId = jwtTokenProvider.getUserIdAndIsValid(getToken(request));
            var userDetails = userDetailService.loadUserByUsername(userId);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (CustomBadRequestException e) {
            sendErrorResponse(response, "토큰이 유효하지 않습니다");
            return;
        } catch (Exception e) {
            sendErrorResponse(response, "회원을 찾을 수 없습니다");
            return;
        }
        filterChain.doFilter(request,response);
    }

    private static void sendErrorResponse(HttpServletResponse response, String s) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(s);
        response.setStatus(400);
    }


    private String getToken(HttpServletRequest request) {
        return jwtTokenProvider.resolveToken(request);
    }

}

