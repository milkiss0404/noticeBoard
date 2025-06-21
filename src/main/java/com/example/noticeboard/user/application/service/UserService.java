package com.example.noticeboard.user.application.service;

import com.example.noticeboard.user.application.dtos.request.RequestUserDto;
import com.example.noticeboard.user.domain.UserRole;
import com.example.noticeboard.user.domain.interfaces.UserRepository;
import com.example.noticeboard.user.repository.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity createUser(@Valid RequestUserDto dto) {
        if (userRepository.findByUserName(dto.userName()).isPresent()) {
            throw new IllegalArgumentException("존재하는 회원입니다");
        }
        UserEntity userEntity = UserEntity.builder()
                .username(dto.userName())
                .passwd(passwordEncoder.encode(dto.passwd()))
                .role(UserRole.USER)
                .build();
        return userRepository.createUser(userEntity);
    }

    public void login(RequestUserDto request) throws LoginException {
        UserEntity user = userRepository.findByUserName(request.userName())
                .orElseThrow(() -> new LoginException("없는 아이디"));

        if (!passwordEncoder.matches(request.passwd(), user.getPasswd())) {
            throw new LoginException("틀린 비밀번호");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없음 " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();

        UserRole role = user.getRole();

        if (role.toString().equals("ADMIN")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 기본 권한

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswd(),
                authorities
        );
    }
}
