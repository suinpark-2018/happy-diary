package com.happydiary.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    int cno; // 댓글 번호
    int bno; // 게시물 번호
    Integer parent_cno; // 상위 댓글
    String writer; // 작성자
    String content; // 내용
    String status; // 삭제여부

    // 시스템 컬럼
    String reg_id;
    String reg_date;
    String up_id;
    String up_date;

    private List<CommentDto> replies; // 답글 목록

    public CommentDto(Integer bno, Integer parent_cno, String writer, String content) {
        this.bno = bno;
        this.parent_cno = parent_cno;
        this.writer = writer;
        this.content = content;
    }

    public CommentDto(Integer cno, Integer bno, Integer parent_cno, String writer, String content) {
        this.cno = cno;
        this.bno = bno;
        this.parent_cno = parent_cno;
        this.writer = writer;
        this.content = content;
    }
}
