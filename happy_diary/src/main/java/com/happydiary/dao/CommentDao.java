package com.happydiary.dao;

import com.happydiary.dto.CommentDto;

import java.util.List;

public interface CommentDao {
    // 전체 데이터 갯수 카운트
    int countAll() throws Exception;

    // 활성화 상태의 댓글 수 카운트
    int countActiveCmt() throws Exception;

    // 댓글 추가
    int insert(CommentDto commentDto) throws Exception;

    // 특정 댓글 조회
    CommentDto select(int cno) throws Exception;

    // 전체 게시물의 전체 댓글 및 대댓글 조회
    List<CommentDto> selectActiveAll() throws Exception;

    // 특정 게시물의 댓글 조회
    List<CommentDto> selectAllComment(int bno) throws Exception;

    // 특정 댓글의 답글(대댓글) 조회
    List<CommentDto> selectAllReply(int cno) throws Exception;

    // 댓글 수정
    int update(CommentDto commentDto) throws Exception;

    // 특정 댓글 삭제
    int delete(int cno) throws Exception;

    // 전체 댓글 삭제
    int deleteAll() throws Exception;
}
