package com.happydiary.controller;

import com.happydiary.dto.CommentDto;
import com.happydiary.service.BoardServiceImpl;
import com.happydiary.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/comment", produces = "application/json;charset=UTF-8")
public class CommentController {
    @Autowired CommentServiceImpl commentService;
    @Autowired BoardServiceImpl boardService;

    // 댓글 달기
    @PostMapping("/add")
    public String add(CommentDto commentDto, int pno, String visibility) {
        commentService.writeComment(commentDto);
        return "redirect:/board/detail?bno=" + commentDto.getBno() + "&pno=" + pno + "&visibility=" + visibility;
    }

    // 대댓글(=답글) 달기
    @PostMapping("/reply")
    public String reply(int pno, CommentDto commentDto, String visibility) {
        commentService.writeComment(commentDto);
        return "redirect:/board/detail?bno=" + commentDto.getBno() + "&pno=" + pno + "&visibility=" + visibility;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<String> update(@RequestBody Map<String, Object> requestBody) {
        int cno = Integer.parseInt(requestBody.get("cno").toString());
        String content = (String) requestBody.get("content");

        CommentDto comment = commentService.getCommentByCno(cno);
        int bno = comment.getBno();
        Integer parent_cno = comment.getParent_cno();
        String writer = comment.getWriter();

        CommentDto updatedCmt = new CommentDto(cno, bno, parent_cno, writer, content);

        try {
            if (commentService.modifyCommentOrReply(updatedCmt)) {
                System.out.println("수정 성공!!");
                return new ResponseEntity<>("댓글 또는 답글 수정 성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("댓글 또는 답글 수정 실패", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return new ResponseEntity<>("댓글 또는 답글 수정 시 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestBody Map<String, Integer> requestBody) {
        int cno = requestBody.get("cno");
        try {
            if (commentService.getCommentByCno(cno) != null) {
                commentService.removeCommentOrReply(cno);
                return new ResponseEntity<>("댓글 또는 답글 삭제 성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("댓글 또는 답글 삭제 실패", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return new ResponseEntity<>("댓글 또는 답글 삭제 시 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
