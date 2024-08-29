package com.example.noticeboard.DTO;

import com.example.noticeboard.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor //기본생성자
@AllArgsConstructor //모든 필드를 매개변수로 하는 생성자
@Builder
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private MultipartFile boardFile; //파일 담아줄수있는 interface
    private String originalFileName;   //원본파일이름
    private String storedFileName; //서버 저장용 파일이름
    private int fileAttached; //파일 첨부여부 (1 첨부 , 0 미첨부)


    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        return BoardDTO.builder()
                .id(boardEntity.getId())
                .boardWriter(boardEntity.getBoardWriter())
                .boardPass(boardEntity.getBoardPass())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContents(boardEntity.getBoardContent())
                .boardHits(boardEntity.getBoardHits())
                .boardCreatedTime(boardEntity.getCreatedTime())
                .boardUpdatedTime(boardEntity.getUpdatedTime())
                .build();
    }




}
