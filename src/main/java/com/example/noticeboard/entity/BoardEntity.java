package com.example.noticeboard.entity;

import com.example.noticeboard.DTO.BoardDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)// auto_increment
    private Long id;

    @Column(length = 20, nullable = false)
    private String boardWriter;

    @Column //default는 크기 255, null 가능
    private String boardPass;
    @Column
    private String boardTitle;
    @Column(length = 500)
    private String boardContent;
    @Column
    private int boardHits;
    @Column
    private int fileAttached; //1 or 0

    // cascade = CascadeType.REMOVE,orphanRemoval = true가 delete cascade주는것과 같다
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();


    public static BoardEntity toSaveEntity(BoardDTO boardDTO){
    return BoardEntity.builder()
            .boardWriter(boardDTO.getBoardWriter())
            .boardPass(boardDTO.getBoardPass())
            .boardTitle(boardDTO.getBoardTitle())
            .boardContent(boardDTO.getBoardContents())
            .boardHits(0)
            .fileAttached(0) //파일 없음
            .build();
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO){
    return BoardEntity.builder()
            .id(boardDTO.getId())
            .boardWriter(boardDTO.getBoardWriter())
            .boardPass(boardDTO.getBoardPass())
            .boardTitle(boardDTO.getBoardTitle())
            .boardContent(boardDTO.getBoardContents())
            .boardHits(boardDTO.getBoardHits())
            .build();
    }

    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
        return BoardEntity.builder()
                .boardWriter(boardDTO.getBoardWriter())
                .boardPass(boardDTO.getBoardPass())
                .boardTitle(boardDTO.getBoardTitle())
                .boardContent(boardDTO.getBoardContents())
                .boardHits(0)
                .fileAttached(1) //파일 있음
                .build();
    }
}
