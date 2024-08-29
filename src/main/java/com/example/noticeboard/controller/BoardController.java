package com.example.noticeboard.controller;

import com.example.noticeboard.DTO.BoardDTO;
import com.example.noticeboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {  //html  에서 name이 DTO와 같다면 담는다
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model){
        //DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다
       List<BoardDTO>boardDTOList =  boardService.findAll();
       model.addAttribute("boardList",boardDTOList);
       return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page = 1)Pageable pageable){
        /*
        * 해당 게시글의 조회수를 하나 올리고
        * 게시글 데이터를 가져와서 detail.html에 출력*/
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findByid(id);
        model.addAttribute("board",boardDTO);
        model.addAttribute("page",pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id , Model model){
        BoardDTO boardDTO = boardService.findByid(id);
        model.addAttribute("boardUpdate",boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO,Model model){
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board",board);
        return "redirect:/board/"+ boardDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/";
    }

    @GetMapping("/paging")
    //Pageable,Page == springframework 꺼 써야함
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model){
        Page<BoardDTO> boardList = boardService.paging(pageable);
        //page갯수 20개 현재사용자가 3페이지
        // 1 2 3 4 5
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList",boardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "paging";
    }
}
