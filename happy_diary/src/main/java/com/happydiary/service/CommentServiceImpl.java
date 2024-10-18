package com.happydiary.service;

import com.happydiary.dao.CommentDaoImpl;
import com.happydiary.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired CommentDaoImpl commentDao;

    // 삭제여부 상관없이 모든 댓글 및 답글 개수 확인
    @Override
    public int getNumberOfAllCmt() {
        int total = 0;
        try {
            total = commentDao.countAll();
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return total;
    }

    // 활성화된 전체 댓글 개수 확인
    @Override
    public int getNumberOfActiveCmt() {
        int total = 0;
        try {
            total = commentDao.countActiveCmt();
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return total;
    }

    // 활성화된 댓글, 답글 전체 조회
    @Override
    public List<CommentDto> getAllActiveCmt() {
        List<CommentDto> comments = new ArrayList<>();
        try {
            comments = commentDao.selectActiveAll();
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return comments;
    }

    // 특정 댓글 조회
    @Override
    public CommentDto getCommentByCno(int cno) {
        CommentDto commentDto = new CommentDto();
        try {
            commentDto = commentDao.select(cno);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return commentDto;
    }

    // 각 게시물의 전체 댓글 및 대댓글 조회
    @Override
    public List<CommentDto> getAllCmtOfBoard(int bno) {
        List<CommentDto> comments = new ArrayList<>();
        try {
            comments = commentDao.selectAllComment(bno);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return comments;
    }

    // 각 댓글의 전체 답글(=대댓글) 조회
    @Override
    public List<CommentDto> getAllReply(int cno) {
        List<CommentDto> replies = new ArrayList<>();
        try {
            replies = commentDao.selectAllReply(cno);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return replies;
    }

    // 댓글 추가
    @Override
    public boolean writeComment(CommentDto commentDto) {
        boolean successToWrite = true;
        try {
            commentDao.insert(commentDto);
        } catch (Exception e) {
            successToWrite = false;
            e.printStackTrace();
            e.getMessage();
        }
        return successToWrite;
    }

    // 댓글 또는 답글 내용 수정
    @Override
    public boolean modifyCommentOrReply(CommentDto commentDto) {
        boolean successToModify = true;
        try {
            if (commentDto.getContent() != null) {
                commentDao.update(commentDto);
            } else {
                successToModify = false;
            }
        } catch (Exception e) {
            successToModify = false;
            e.printStackTrace();
            e.getMessage();
        }
        return successToModify;
    }

    // 댓글 또는 답글 삭제
    // 댓글 삭제되면 그에 대한 답글도 같이 삭제
    @Override
    public boolean removeCommentOrReply(int cno) {
        boolean successToRemove = true;
        try {
            if (commentDao.select(cno) != null) {
                commentDao.delete(cno);
            } else {
                successToRemove = false;
            }
        } catch (Exception e) {
            successToRemove = false;
            e.printStackTrace();
            e.getMessage();
        }
        return successToRemove;
    }

    // 전체 댓글 삭제
    @Override
    public boolean removeAllCommentOrReply() {
        boolean successToRemoveAll = true;
        try {
            commentDao.deleteAll();
        } catch (Exception e) {
            successToRemoveAll = false;
            e.printStackTrace();
            e.getMessage();
        }
        return successToRemoveAll;
    }
}
