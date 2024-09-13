package com.happydiary.service;

import com.happydiary.dto.CommentDto;

import java.util.List;

public interface CommentService {
    // 삭제여부 상관없이 모든 댓글 및 답글 개수 확인
    int getNumberOfAllCmt();

    // 전체 댓글 개수 확인
    int getNumberOfActiveCmt();

    // 활성화된 댓글, 답글 전체 조회
    List<CommentDto> getAllActiveCmt();

    // 특정 댓글 조회
    CommentDto getCommentByCno(int cno);

    // 각 게시물의 전체 댓글 조회
    List<CommentDto> getAllCmtOfBoard(int bno);

    // 각 댓글의 전체 답글(=대댓글) 조회
    List<CommentDto> getAllReply(int cno);

    // 댓글 추가
    boolean writeComment(CommentDto commentDto);

    // 댓글 또는 답글 수정
    boolean modifyCommentOrReply(CommentDto commentDto);

    // 댓글 또는 답글 삭제
    // 댓글 삭제되면 그에 대한 답글도 같이 삭제
    boolean removeCommentOrReply(int cno);

    // 전체 댓글 삭제
    boolean removeAllCommentOrReply();
}
