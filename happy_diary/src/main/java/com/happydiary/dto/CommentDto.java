package com.happydiary.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    Integer cno;
    Integer bno;
    Integer parent_cno;
    String writer;
    String content;
    String reg_id;
    String reg_date;
    String up_id;
    String up_date;

    @Override
    public String toString() {
        return "CommentDto{" +
                "cno=" + cno +
                ", bno=" + bno +
                ", parent_cno=" + parent_cno +
                ", writer='" + writer + '\'' +
                ", content='" + content + '\'' +
                ", reg_id='" + reg_id + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", up_id='" + up_id + '\'' +
                ", up_date='" + up_date + '\'' +
                '}';
    }
}
