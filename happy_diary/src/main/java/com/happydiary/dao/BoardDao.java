package com.happydiary.dao;

import com.happydiary.dto.BoardDto;
import com.happydiary.dto.PageRequestDto;

import java.util.List;

public interface BoardDao {
    // 전체 게시물 갯수
    int count() throws Exception;

    // 게시물 쓰기
    int insert(BoardDto boardDto) throws Exception;

    // 게시물 조회
    List<BoardDto> selectAll(PageRequestDto pageRequestDto) throws Exception;

    // 제목, 작성자로 특정 게시물 조회 시 행 개수
    int countSelectedRow(String option, String keyword) throws Exception;

    // 제목, 작성자로 특정 게시물 조회 결과 (페이징 처리)
    List<BoardDto> selectByTitleOrWriter(PageRequestDto pageRequestDto, String option, String keyword) throws Exception;

    // 공개여부에 따른 게시물 개수
    int countSelectedRowByVisibleScope(String visibility) throws Exception;

    // 게시물 공개여부에 따른 조회 결과
    List<BoardDto> selectByVisibleScope(PageRequestDto pageRequestDto, String visibility) throws Exception;

    // 칭찬 대상에 따른 게시물 개수
    int countSelectedRowByTarget(String target) throws Exception;

    // 칭찬 대상에 따른 게시물 목록 조회 결과
    List<BoardDto> selectByTarget(PageRequestDto pageRequestDto, String praise_target) throws Exception;

    // 특정 게시물 조회
    BoardDto select(Integer bno) throws Exception;

    // 게시물 수정
    int update(BoardDto boardDto) throws Exception;

    // 게시물 삭제
    int delete(Integer bno) throws Exception;

    // 게시물 전체 삭제
    int deleteAll() throws Exception;
}
