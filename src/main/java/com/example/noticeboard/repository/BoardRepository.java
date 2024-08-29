package com.example.noticeboard.repository;

import com.example.noticeboard.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//JpaRepository<엔티티 클래스, pk의 타입>

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    //엔티티는 약어 필수
    //:id 는 Param 의 id와 같다.
    @Modifying //update나 delete 같은 쿼리 쓸때 필수 어노테이션
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
}
