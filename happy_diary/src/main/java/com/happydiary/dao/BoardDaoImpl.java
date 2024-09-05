package com.happydiary.dao;

import com.happydiary.dto.BoardDto;
import com.happydiary.dto.PageRequestDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDaoImpl implements BoardDao {
    @Autowired SqlSession session;

    private static String namespace = "com.happydiary.dao.BoardDao.";

    // 전체 게시물 갯수
    @Override
    public int count() throws Exception {
        return session.selectOne(namespace + "count");
    }

    // 게시물 쓰기
    @Override
    public int insert(BoardDto boardDto) throws Exception {
        return session.insert(namespace + "insert", boardDto);
    }

    // 게시물 조회
    @Override
    public List<BoardDto> selectAll(PageRequestDto pageRequestDto) throws Exception {
        return session.selectList(namespace + "selectAll", pageRequestDto);
    }

    // 게시물 검색
    // 제목, 작성자에 따른 게시물 개수
    @Override
    public int countSelectedRow(String option, String keyword) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("option", option);
        map.put("keyword", keyword);
        return session.selectOne(namespace + "countSelectedRow", map);
    }

    // 제목, 작성자로 특정 게시물 조회 결과 (페이징 처리)
    @Override
    public List<BoardDto> selectByTitleOrWriter(PageRequestDto pageRequestDto, String option, String keyword) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("skip", pageRequestDto.getSkip());
        map.put("size", pageRequestDto.getSize());
        map.put("option", option);
        map.put("keyword", keyword);
        return session.selectList(namespace + "selectByTitleOrWriter", map);
    }

    // 공개여부에 따른 게시물 개수
    @Override
    public int countSelectedRowByVisibleScope(String visibility) throws Exception {
        return session.selectOne(namespace + "countSelectedRowByVisibleScope", visibility);
    }

    // 게시물 공개여부에 따른 게시물 목록 조회 결과
    @Override
    public List<BoardDto> selectByVisibleScope(PageRequestDto pageRequestDto, String visibility) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("skip", pageRequestDto.getSkip());
        map.put("size", pageRequestDto.getSize());
        map.put("visibility", visibility);
        return session.selectList(namespace + "selectByVisibleScope", map);
    }

    // 칭찬 대상에 따른 게시물 개수
    @Override
    public int countSelectedRowByTarget(String praise_target) throws Exception {
        return session.selectOne(namespace + "countSelectedRowByTarget", praise_target);
    }

    // 칭찬 대상에 따른 게시물 목록 조회 결과
    @Override
    public List<BoardDto> selectByTarget(PageRequestDto pageRequestDto, String praise_target) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("skip", pageRequestDto.getSkip());
        map.put("size", pageRequestDto.getSize());
        map.put("praise_target", praise_target);
        return session.selectList(namespace + "selectByTarget", map);
    }

    // 특정 게시물 조회
    @Override
    public BoardDto select(Integer bno) throws Exception {
        return session.selectOne(namespace + "select", bno);
    }

    // 게시물 수정
    @Override
    public int update(BoardDto boardDto) throws Exception {
        return session.update(namespace + "update", boardDto);
    }

    // 게시물 삭제
    @Override
    public int delete(Integer bno) throws Exception {
        return session.delete(namespace + "delete", bno);
    }

    // 게시물 전체 삭제
    @Override
    public int deleteAll() throws Exception {
        return session.delete(namespace + "deleteAll");
    }
}