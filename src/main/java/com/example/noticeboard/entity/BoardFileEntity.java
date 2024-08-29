package com.example.noticeboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;


    @ManyToOne(fetch = FetchType.LAZY) //자식은 여럿 부모는 하나 관계니까 ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;
}
