package com.happydiary.dao;

import com.happydiary.dto.BoardDto;
import com.happydiary.dto.PageRequestDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BoardDaoImpl implements BoardDao {

    private final SqlSession session;
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
    public int countSelectedRowByVisibleScope(String visibility, String id) throws Exception {
        // 공개범위가 유효하지 않은 값으로 들어오면 0 반환
        if (!visibility.equals("public") && !visibility.equals("private")) {
            return 0;
        }
        // 공개범위 및 사용자 아이디
        Map<String, String> map = new HashMap<>();
        map.put("visibility", visibility);
        map.put("id", id);
        return session.selectOne(namespace + "countSelectedRowByVisibleScope", map);
    }

    // 게시물 공개여부에 따른 게시물 목록 조회 결과
    // Public 의 경우, 어떤 사용자든 상관없이 모든 공개 게시물 출력
    // Private 의 경우, 로그인한 아이디로 조회하여 해당 사용자가 작성한 게시물만 출력
    @Override
    public List<BoardDto> selectByVisibleScope(PageRequestDto pageRequestDto, String visibility, String id) throws Exception {
        // 공개범위가 유효하지 않은 값으로 들어오면 빈 리스트 반환
        if (!visibility.equals("public") && !visibility.equals("private")) {
            return new ArrayList<>();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("skip", pageRequestDto.getSkip());
        map.put("size", pageRequestDto.getSize());
        map.put("visibility", visibility);
        map.put("id", id);
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
    // is_active Y -> N 으로 업데이트 하여 일정 기간동안 보관
    @Override
    public int delete(Integer bno) throws Exception {
        return session.update(namespace + "delete", bno);
    }

    // 게시물 전체 삭제
    @Override
    public int deleteAll() throws Exception {
        return session.delete(namespace + "deleteAll");
    }
}