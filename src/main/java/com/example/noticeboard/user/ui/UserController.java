package com.example.noticeboard.user.ui;

import com.example.noticeboard.common.JwtTokenProvider;
import com.example.noticeboard.common.Response;
import com.example.noticeboard.common.redis.RedisService;
import com.example.noticeboard.token.service.RefreshTokenService;
import com.example.noticeboard.user.application.dtos.request.RequestUserDto;
import com.example.noticeboard.user.application.dtos.response.ResponseUserDto;
import com.example.noticeboard.user.application.service.UserService;
import com.example.noticeboard.user.repository.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import java.nio.file.attribute.UserPrincipal;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RedisService redisService;

    @PostMapping("/join")
    public Response<ResponseUserDto> createUser(@RequestBody RequestUserDto dto) {
        UserEntity user = userService.createUser(dto);
        return Response.ok(ResponseUserDto.from(user));
    }


    @PostMapping("/login")
    public Response<String> login(@RequestBody RequestUserDto request, HttpServletResponse response) throws LoginException {

            UserEntity userEntity = userService.login(request);


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.userName(), request.passwd())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        String accessToken = jwtTokenProvider.createAccessToken(username);
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        String key = "refreshToken:userId %s".formatted(userEntity.getUsername());
        redisService.saveData(key,refreshToken);
//        refreshTokenService.save(username, refreshToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .build();

        //setHeader 는 겹쳐서 1개만나옴
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        return Response.ok("로그인 성공");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);

        if (!jwtTokenProvider.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Refresh Token 이 만료되었습니다. 다시 로그인 해주세요."));
        }
        String username = jwtTokenProvider.getUserId(refreshToken);

        if (!refreshTokenService.isValid(refreshToken, username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token not found or mismatch");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(username);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken
        ));
    }
    }
