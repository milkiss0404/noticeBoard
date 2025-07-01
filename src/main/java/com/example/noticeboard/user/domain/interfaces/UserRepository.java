package com.example.noticeboard.user.domain.interfaces;

import com.example.noticeboard.user.repository.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity createUser(UserEntity user);

    Optional<UserEntity> findByUserName(String userName);

    List<UserEntity> findAll();
}
