package com.happydiary.controller;

import com.happydiary.dto.BoardDto;
import com.happydiary.dto.CommentDto;
import com.happydiary.dto.PageRequestDto;
import com.happydiary.dto.PageResponseDto;
import com.happydiary.service.BoardServiceImpl;
import com.happydiary.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static java.util.Collections.reverse;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardServiceImpl boardService;
    private final CommentServiceImpl commentService;

    // 게시판 첫 화면으로 이동
    @GetMapping("/home")
    public String home() {
        return "main";
    }

    // Pagination 위한 객체 반환
    // 매개변수: 조회된 게시물 목록 및 총 갯수 포함
    private PageResponseDto<BoardDto> getPageResponseDto(PageRequestDto pageRequestDto, List<BoardDto> boards, int total) {
        return new PageResponseDto<>(pageRequestDto, boards, total);
    }

    // 공개범위별 게시판 내 게시물 목록 반환
    @GetMapping("/list")
    public String list(int pno, String visibility, Model model, HttpServletRequest request) {
        // 현재 페이지 및 공개범위
        PageRequestDto pageRequestDto = new PageRequestDto(pno, 10);

        // 현재 로그인한 사용자 아이디
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("userId");

        // 특정 페이지 및 공개범위, 아이디를 기준으로 조회된 게시물의 목록 및 총 갯수
        List<BoardDto> boards = boardService.findByVisibleScope(pno, visibility, id);
        int total = boardService.getNumberOfFoundBoardsByVisibility(visibility, id);

        if (!boards.isEmpty()) {
            model.addAttribute("visibility", visibility);
            model.addAttribute("boards", boards);
            model.addAttribute("pageDto", getPageResponseDto(pageRequestDto, boards, total));
        } else {
            System.out.println("BoardDto is empty.");
        }
        return "board";
    }

    @GetMapping("/detail")
    public String detail(int bno, String visibility, int pno, Model model, HttpSession session) {
        // 게시물
        BoardDto boardDto = boardService.getBoard(bno);

        // 특정 게시물에 대한 댓글 목록 조회
        List<CommentDto> comments = commentService.getAllCmtOfBoard(bno);
        for (CommentDto comment : comments) {
            int cno = comment.getCno();
            List<CommentDto> replies = commentService.getAllReply(cno);
            reverse(replies);
            comment.setReplies(replies);
        }

        // 로그인한 사용자와 작성자와 일치하는지 확인
        String userId = (String) session.getAttribute("userId");
        String writer = boardDto.getWriter();
        boolean isAuthor = userId.equals(writer);

        // 게시물 조회수 업데이트
        // 다른 사용자가 조회 시 1 증가
        Integer view_cnt = boardDto.getView_cnt();
        if (!isAuthor) {
            view_cnt++;
            boardService.updateCurrentViewCnt(bno, view_cnt);
        }
        BoardDto updatedDto = boardService.getBoard(bno);

        // model 객체에 데이터 담아서 view 단으로 전송
        model.addAttribute("pno", pno);
        model.addAttribute("visibility", visibility);
        model.addAttribute("board", updatedDto);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("comments", comments);

        return "boardDetail";
    }

    // 새로운 게시물 작성
    // 게시물 작성을 위한 페이지로 이동
    @GetMapping("/create")
    public String moveToCreate(String visibility, Model model, HttpServletRequest request) {
        // 현재 로그인한 사용자 아이디
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("userId");

        model.addAttribute("userId", id);
        model.addAttribute("visibility", visibility);
        return "createBoard";
    }

    // 게시물 작성
    @PostMapping("/create")
    public String createBoard(BoardDto boardDto) {
        boardService.makeBoard(boardDto);
        String is_public = boardDto.getIs_public();

        String visibility = is_public.equals("Y") ? "public" : "private";

        return "redirect:/board/list?visibility=" + visibility + "&pno=1";
    }

    // 게시물 수정
    // 게시물 수정 페이지로 이동
    @GetMapping("/modify")
    public String moveToModify(int bno, int pno, Model model) {
        BoardDto boardDto = boardService.getBoard(bno);
        String visibility = boardDto.getIs_public().equals("Y") ? "public" : "private";
        model.addAttribute("board", boardDto);
        model.addAttribute("pno", pno);
        model.addAttribute("visibility", visibility);
        return "modifyBoard";
    }

    @PostMapping("/modify")
    public String modifyBoard(BoardDto boardDto, int pno) {
        boardService.modifyBoard(boardDto);

        int bno = boardDto.getBno();
        String visibility = boardDto.getIs_public().equals("Y") ? "public" : "private";

        return "redirect:/board/detail?visibility=" + visibility + "&bno=" + bno + "&pno=" + pno;
    }

    // 게시물 삭제
    @GetMapping("/delete")
    public String delete(String visibility, int bno, int pno) {
        boardService.removeBoard(bno);
        return "redirect:/board/list?visibility=" + visibility + "&pno=" + pno;
    }

    // 검색
    @GetMapping("/find")
    public String find(@RequestParam(value = "pno", defaultValue = "1") int pno, String visibility, String option, String keyword, Model model) {
        keyword = String.join("", keyword.split(" "));

        PageRequestDto pageRequestDto = new PageRequestDto(pno, 10);
        List<BoardDto> foundBoards = boardService.findByTitleOrWriter(pno, option, keyword);
        int total = boardService.getNumberOfFoundBoards(option, keyword);
        PageResponseDto<BoardDto> pageResponseDto = new PageResponseDto<>(pageRequestDto, foundBoards, total);

        model.addAttribute("pno", pno);
        model.addAttribute("visibility", visibility);
        model.addAttribute("foundBoards", foundBoards);
        model.addAttribute("pageDto", pageResponseDto);
        model.addAttribute("option", option);
        model.addAttribute("keyword", keyword);
        return "foundBoards";
    }

    @GetMapping("/notice")
    public String notice() {
        return "notice";
    }
}