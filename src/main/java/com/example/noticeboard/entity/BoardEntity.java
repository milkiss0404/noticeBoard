package com.example.noticeboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)// auto_increment
    private Long id;

    @Column(length = 20, nullable = false)
    private String boardWriter;
}
