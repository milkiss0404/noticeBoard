package com.example.noticeboard.controller;

import com.example.noticeboard.DTO.CommentDTO;
import com.example.noticeboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    @ResponseBody
    public String save(@ModelAttribute CommentDTO commentDTO){
        commentService.save(commentDTO);
        return "ok";
    }
}
