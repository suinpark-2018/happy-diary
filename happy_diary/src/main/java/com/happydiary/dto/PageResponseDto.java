package com.happydiary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDto<E> {
    private int page;  // 현재 페이지 번호
    private int size;  // 한 페이지 당 게시물 개수
    private int total; // 전체 게시물 개수

    private int start; // 시작 페이지 번호
    private int end;   // 끝 페이지 번호

    private boolean prev; // 이전 페이지 존재여부
    private boolean next; // 다음 페이지 존재여부

    private List<E> boards;

    @Builder(builderMethodName = "withAll")
    public PageResponseDto(PageRequestDto pageRequestDto, List<E> boards, int total) {
        this.page = pageRequestDto.getPage();
        this.size = pageRequestDto.getSize();

        this.total = total;
        this.boards = boards;

        this.end = (int) (Math.ceil(this.page / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil((total / (double) size)));
        this.end = end > last ? last : end;

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
