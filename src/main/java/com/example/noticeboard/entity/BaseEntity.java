package com.example.noticeboard.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 해당 Entity를 부모클래스로 만든다 라고 이해하면될듯?
@EntityListeners(AuditingEntityListener.class)//Entity가 삽입,삭제,수정,조회 등의 작업을 할때 전,후에 어떠한 작업을 하기위해 이벤트 처리를위한 어노테이션
@Getter
public class BaseEntity {
    @CreationTimestamp // creat가 발생했을 때 -> 생성했을때 시간정보를 줄수있다.
    @Column(updatable = false) // 수정->update시에는 관여 x
    private LocalDateTime createdTime;

    @UpdateTimestamp // update 가 발생했을때 시간정보를 줄수있다
    @Column(insertable = false)// insert시에는 관여 x
    private LocalDateTime updatedTime;

}

//
//@MappedSuperClass // @Entity 대신 사용
//public abstract class BaseEntity {
//
//    @Id
//    @GeneratedValue
//    private Long id;
//    private String name;
//
//    ...
//}
//
//-----------------------------------
//
//@Entity
//public class Member extends BaseEntity {
//
//    // ID, name 상속받음
//    private String email;
//
//    ...
//}
//
//@Entity
//public class Seller extends BaseEntity {
//
//    // ID, name 상속받음
//    private String shopName;
//
//    ...
//}