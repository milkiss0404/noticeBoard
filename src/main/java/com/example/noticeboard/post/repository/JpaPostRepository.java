package com.example.noticeboard.post.repository;

import com.example.noticeboard.post.repository.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaPostRepository extends JpaRepository<PostEntity,Long> {
//
//    @Query("SELECT p from PostEntity p order by p.createDateTime desc")
//    List<PostEntity> findAllPost();


    void removeById(Long id);
}
