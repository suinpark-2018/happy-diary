package com.happydiary.dao;

import com.happydiary.dto.CommentDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDaoImpl implements CommentDao{
    @Autowired SqlSession session;

    private static String namespace = "com.happydiary.dao.CommentDao.";

    // 전체 데이터 갯수 카운트
    @Override
    public int countAll() throws Exception {
        return session.selectOne(namespace + "countAll");
    }

    // 활성화 상태의 댓글 수 카운트
    @Override
    public int countActiveCmt() throws Exception {
        return session.selectOne(namespace + "countActiveCmt");
    }

    // 댓글 추가
    @Override
    public int insert(CommentDto commentDto) throws Exception {
        return session.insert(namespace + "insert", commentDto);
    }

    // 특정 댓글 조회
    @Override
    public CommentDto select(int cno) throws Exception {
        return session.selectOne(namespace + "select", cno);
    }

    // 전체 게시물의 전체 댓글 및 대댓글 조회
    @Override
    public List<CommentDto> selectActiveAll() throws Exception {
        return session.selectList(namespace + "selectActiveAll");
    }

    // 특정 게시물의 댓글 및 대댓글 조회
    @Override
    public List<CommentDto> selectAllComment(int bno) throws Exception {
        return session.selectList(namespace + "selectAllComment", bno);
    }

    // 특정 댓글의 답글(대댓글) 조회
    @Override
    public List<CommentDto> selectAllReply(int cno) throws Exception {
        return session.selectList(namespace + "selectAllReply", cno);
    }

    // 댓글 수정
    @Override
    public int update(CommentDto commentDto) throws Exception {
        return session.update(namespace + "update", commentDto);
    }

    // 특정 댓글 삭제
    @Override
    public int delete(int cno) throws Exception {
        return session.update(namespace + "delete", cno);
    }

    // 전체 댓글 삭제
    @Override
    public int deleteAll() throws Exception {
        return session.delete(namespace + "deleteAll");
    }
}
