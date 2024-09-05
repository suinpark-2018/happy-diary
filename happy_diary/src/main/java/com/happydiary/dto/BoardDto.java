package com.happydiary.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    Integer bno;
    String title;
    String content;
    String img_path;
    String writer;
    String praise_target;
    String is_public;
    Integer view_cnt;
    Integer comment_cnt;
    String is_notice;
    String reg_id;
    String reg_date;
    String up_id;
    String up_date;

    // 게시물 작성
    public BoardDto(String title, String content, String img_path, String writer, String praise_target, String is_public, String is_notice) {
        this.title = title;
        this.content = content;
        this.img_path = img_path;
        this.writer = writer;
        this.praise_target = praise_target;
        this.is_public = is_public;
        this.is_notice = is_notice;
    }

    // 게시물 수정
    public BoardDto(Integer bno, String title, String content, String img_path, String praise_target, String is_public) {
        this.bno = bno;
        this.title = title;
        this.content = content;
        this.img_path = img_path;
        this.praise_target = praise_target;
        this.is_public = is_public;
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "bno=" + bno +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", img_path='" + img_path + '\'' +
                ", writer='" + writer + '\'' +
                ", praise_target='" + praise_target + '\'' +
                ", is_public='" + is_public + '\'' +
                ", view_cnt=" + view_cnt +
                ", comment_cnt=" + comment_cnt +
                ", is_notice='" + is_notice + '\'' +
                ", reg_id='" + reg_id + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", up_id='" + up_id + '\'' +
                ", up_date='" + up_date + '\'' +
                '}';
    }
}
