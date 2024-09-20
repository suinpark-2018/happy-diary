package com.happydiary.service;

import com.happydiary.dto.BoardDto;

import java.util.List;

public interface BoardService {
    // 게시물 개수 확인
    int getNumberOfBoards();

    // 페이지에 따른 전체 게시물 조회
    // pno: 페이지 번호, size: 한 페이지 당 게시물 개수
    List<BoardDto> getListOfBoards(int pno);

    // 특정 게시물 조회
    BoardDto getBoard(int bno);

    // 1.1. Option (Title or Writer) 기준으로 검색된 게시물 개수
    // Overload (오버로딩) 사용 - 매개변수 개수 다르게 지정
    int getNumberOfFoundBoards(String option, String keyword);

    // 1.2. Option (Title or Writer) 에 따라 조회된 게시물 목록
    List<BoardDto> findByTitleOrWriter(int pno, String option, String keyword);

    // 2.1. 공개여부(Public or Private) 기준에 따라 검색된 게시물 개수
    int getNumberOfFoundBoardsByVisibility(String visibility, String id);

    // 2.2. 공개여부(Public or Private) 기준으로 조회된 게시물 목록
    List<BoardDto> findByVisibleScope(int pno, String visibility, String id);

    // 3.1. 칭찬 대상(praise_target) 기준으로 검색된 게시물 개수
    int getNumberOfFoundBoardsByTarget(String praise_target);

    // 3.2. 칭찬 대상(praise_target) 기준으로 조회된 게시물 목록
    List<BoardDto> findByPraiseTarget(int pno, String prise_target);

    // 게시물 작성
    boolean makeBoard(BoardDto boardDto);

    // 게시물 수정
    boolean modifyBoard(BoardDto boardDto);

    // 게시물 조회수 업데이트
    boolean updateCurrentViewCnt(int bno, int view_cnt);

    // 특정 게시물 삭제
    boolean removeBoard(int bno);

    // 전체 게시물 삭제
    boolean removeAllBoards();
}
