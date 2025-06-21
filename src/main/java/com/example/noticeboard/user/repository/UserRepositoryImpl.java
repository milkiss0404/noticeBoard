package com.example.noticeboard.user.repository;

import com.example.noticeboard.user.repository.entity.UserEntity;
import com.example.noticeboard.user.domain.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        return jpaUserRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findByUserName(String userName) {
        return jpaUserRepository.findByUsername(userName);
    }
}
