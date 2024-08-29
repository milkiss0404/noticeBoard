package com.example.noticeboard.service;

import com.example.noticeboard.DTO.BoardDTO;
import com.example.noticeboard.entity.BoardEntity;
import com.example.noticeboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//service클래스에서는 주로 jpa를 사용할때 DTO-> Entity ,Entity-> DTO 로 변환 많이함
// repository.메소드 파라미터가 entity 인경우,
//컨트롤러에서 받을때는 DTO로받고 repository르 넘겨줄때는 Entity로 넘겨주고
//쿼리 결과는 Entity로 받아옴 근데그걸  컨트롤러로 리턴해줄때는  DTO로 변환해주어야함

//서비스단에서 변환을해도되고 DTO에 변환ㅂ메소드를 작성해됨 두번째 방법으로할거임

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;  //boardRepository는  Entity만 받아준다
    public void save(BoardDTO boardDTO) throws IOException {

        if (boardDTO.getBoardFile().isEmpty()) {
            //첨부파일 없음.
            BoardEntity saveEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(saveEntity);
        }else {
            /*
            * 1. DTO에 담긴 파일을 꺼냄
            * 2. 파일의 이름을 가져옴
            * 3. 서버 저장용 이름을 만듦
            * 4. 저장 경로 설정
            * 5. 해당경로에 파일저장
            * 6. board_table에 해당 데이터 save 처리
            * 7. board_file_table에 해당 데이터 save 처리
            * */
            MultipartFile boardFile = boardDTO.getBoardFile(); //1
            String originalFilename = boardFile.getOriginalFilename(); //2
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3. uuid쓰는경우도 있고 그럼
            String savePath ="C:/springboot_img/" +storedFileName;//4
            boardFile.transferTo(new File(savePath));//5
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long saveId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(saveId).get();

        }

    }

    public List<BoardDTO> findAll() {
        // Entity로넘어온 리스트를 DTO로 변환해주어야함
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional //jpa가아닌 따로 쿼리를 쓸경우 Transactional 어노테이션 붙이셈 안붙이면 에러남
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findByid(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            return BoardDTO.toBoardDTO(boardEntity);
        }
        else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
      BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findByid(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1;  //
        int pageLimit = 3; //한 페이지에서 보여줄 글의 갯수
        //한페이지당 3개씩 글을 보여주고 정렬기준은 id 기준으로 내림차순정렬
        //page 위치에있는값은 0부터 시작
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit,Sort.by(Sort.Direction.DESC,"id")));
        //"id" 는 엔티티의값

        //목록: id,writer, title,
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;

    }
}
