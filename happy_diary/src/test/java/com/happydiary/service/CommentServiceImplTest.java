package com.happydiary.service;

import com.happydiary.dto.CommentDto;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
class CommentServiceImplTest {

    @Autowired CommentServiceImpl commentService;

    @BeforeEach
    void setDB() {
        // 첫번째, 두번째 게시판에 댓글 50개씩 추가
        for (int i = 1; i <= 50; i++) {
            CommentDto testDto = new CommentDto(1, null, "test_writer" + i, "test_content" + i);
            assertTrue(commentService.writeComment(testDto));
        }
        for (int i = 1; i <= 50; i++) {
            CommentDto testDto = new CommentDto(2, null, "test_writer" + i, "test_content" + i);
            assertTrue(commentService.writeComment(testDto));
        }

        // 답글 추가
        for (int i = 1; i <= 50; i++) {
            // 100개의 대댓글 추가
            int ptCno1 = commentService.getAllActiveCmt().get(0).getCno();
            CommentDto testDto1 = new CommentDto(1, ptCno1, "test_writer" + i, "test_content" + i);
            assertTrue(commentService.writeComment(testDto1));

            int ptCno2 = commentService.getAllActiveCmt().get(1).getCno();
            CommentDto testDto2 = new CommentDto(1, ptCno2, "test_writer" + i, "test_content" + i);
            assertTrue(commentService.writeComment(testDto2));
        }

        assertEquals(200, commentService.getNumberOfActiveCmt());
    }

    @AfterEach
    void cleanDB() {
        assertTrue(commentService.removeAllCommentOrReply());
    }

    @Test
    @DisplayName("CommentService 주입 성공")
    void successToDIOfCommentService() {
        assertNotNull(commentService);
    }

    @Test
    @DisplayName("삭제유무 상관 없이 전체 댓글 조회 성공")
    void successToGetNumberOfAllCmt() {
        assertEquals(200, commentService.getNumberOfAllCmt());

        int cno = commentService.getAllActiveCmt().get(0).getCno();
        assertTrue(commentService.removeCommentOrReply(cno));

        assertEquals(200, commentService.getNumberOfAllCmt());
    }

    @Test
    @DisplayName("활성화된 댓글 수 조회 성공")
    void successToGetNumberOfActiveCmt() {
        assertEquals(200, commentService.getNumberOfActiveCmt());

        int cno = commentService.getAllActiveCmt().get(0).getCno();
        int replyCnt = commentService.getAllReply(cno).size();

        assertTrue(commentService.removeCommentOrReply(cno));

        assertEquals(200 - 1 - replyCnt, commentService.getNumberOfActiveCmt());
    }

    @Test
    @DisplayName("활성화된 댓글 수 조회 실패")
    void failToGetAllActiveCmt() {
        assertEquals(200, commentService.getNumberOfActiveCmt());

        // 잘못된 정보 들어와서 댓글 저장 실패
        CommentDto testDto = new CommentDto();
        assertFalse(commentService.writeComment(testDto));

        assertNotEquals(201, commentService.getNumberOfActiveCmt());
    }

    @Test
    @DisplayName("특정 댓글 조회 성공")
    void successToGetCommentByCno() {
        int cno = commentService.getAllActiveCmt().get(0).getCno();
        assertNotNull(commentService.getCommentByCno(cno));
    }

    @Test
    @DisplayName("잘못된 댓글 번호로 인한 특정 댓글 조회 실패")
    void failToGetCommentByCno() {
        int cno = 0;
        assertNull(commentService.getCommentByCno(cno));
    }

    @Test
    @DisplayName("특정 게시물의 전체 댓글 및 답글 조회 성공")
    void successToGetAllCmtOfBoard() {
        int bno = commentService.getAllActiveCmt().get(0).getBno();
        assertFalse(commentService.getAllCmtOfBoard(bno).isEmpty());
    }

    @Test
    @DisplayName("잘못된 게시물 번호로 인한 특정 게시물의 전체 댓글 및 답글 조회 실패")
    void failToGetAllCmtOfBoard_wrongBno() {
        int bno = 0;
        assertTrue(commentService.getAllCmtOfBoard(bno).isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 댓글로 인한 특정 게시물의 전체 댓글 및 답글 조회 실패")
    void failToGetAllCmtOfBoard_notExistCmt() {
        int bno = commentService.getAllActiveCmt().get(0).getBno();
        assertFalse(commentService.getAllCmtOfBoard(bno).isEmpty());

        assertTrue(commentService.removeAllCommentOrReply());
        assertTrue(commentService.getAllCmtOfBoard(bno).isEmpty());
    }

    @Test
    @DisplayName("특정 댓글의 답글 조회 성공")
    void successToGetAllReply() {
        int cno = commentService.getAllActiveCmt().get(0).getCno();
        CommentDto testDto = new CommentDto(1, cno, "new_writer", "new_content");
        assertTrue(commentService.writeComment(testDto));
        assertFalse(commentService.getAllReply(cno).isEmpty());
    }

    @Test
    @DisplayName("잘못된 댓글 번호로 인한 특정 댓글의 답글 조회 실패")
    void failToGetAllReply_wrongCno() {
        int cno = 0;
        assertTrue(commentService.getAllReply(cno).isEmpty());
    }

    @Test
    @DisplayName("답글 없는 댓글에 대한 답글 조회 시 실패")
    void failToGetAllReply_notExistReply() {
        int cno = commentService.getAllActiveCmt().get(10).getCno();
        assertTrue(commentService.getAllReply(cno).isEmpty());
    }

    @Test
    @DisplayName("댓글 또는 답글 추가 성공")
    void successToWriteComment() {
        assertEquals(200, commentService.getNumberOfActiveCmt());

        CommentDto testDto = new CommentDto(1, null, "new_writer", "new_content");

        assertTrue(commentService.writeComment(testDto));
        assertEquals(201, commentService.getNumberOfActiveCmt());
    }

    @Test
    @DisplayName("댓글 또는 답글 추가 실패_필수항목 누락")
    void failToWriteComment_null() {
        CommentDto testDto = new CommentDto();
        assertFalse(commentService.writeComment(testDto));
    }

    @Test
    @DisplayName("댓글 또는 답글 추가 실패_컬럼 길이 초과")
    void failToWriteComment_overLength() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("Please be aware that an exception will be thrown if the input exceeds the column size.");
        }
        assertTrue(sb.length() > 20);
        String writer = sb.toString();
        CommentDto testDto = new CommentDto(1, null, writer, "correct_content");

        assertFalse(commentService.writeComment(testDto));
    }

    @Test
    @DisplayName("댓글 또는 답글 수정 성공")
    void successToModifyCommentOrReply() {
        String modifiedContent = "modified_content";
        CommentDto testDto = commentService.getAllActiveCmt().get(0);
        assertNotEquals(modifiedContent, testDto.getContent());

        testDto.setContent(modifiedContent);
        assertTrue(commentService.modifyCommentOrReply(testDto));
        assertEquals(modifiedContent, testDto.getContent());
    }

    @Test
    @DisplayName("댓글 또는 답글 수정 실패_필수항목 누락")
    void failToModifyCommentOrReply_null() {
        CommentDto testDto = new CommentDto();
        assertNull(testDto.getContent());
        assertFalse(commentService.modifyCommentOrReply(testDto));
    }

    @Test
    @DisplayName("댓글 또는 답글 수정 실패_컬럼길이 초과")
    void failToModifyCommentOrReply_overLength() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("Please be aware that an exception will be thrown if the input exceeds the column size.");
        }
        assertTrue(sb.length() > 100);
        String modifiedContent = sb.toString();

        CommentDto testDto = commentService.getAllActiveCmt().get(0);
        testDto.setContent(modifiedContent);

        assertFalse(commentService.modifyCommentOrReply(testDto));
    }

    @Test
    @DisplayName("특정 댓글 및 답글 삭제 성공")
    void successToRemoveCommentOrReply() {
        assertEquals(200, commentService.getNumberOfActiveCmt());

        // 특정 댓글과 해당 댓글의 답글 모두 삭제
        int cno = commentService.getAllActiveCmt().get(0).getCno();
        int replyCnt = commentService.getAllReply(cno).size();
        assertTrue(commentService.removeCommentOrReply(cno));

        assertEquals(199 - replyCnt, commentService.getNumberOfActiveCmt());
    }

    @Test
    @DisplayName("잘못된 댓글 번호로 인한 특정 댓글 및 답글 삭제 실패")
    void failToRemoveCommentOrReply_wrongCno() {
        int cno = 0;
        assertFalse(commentService.removeCommentOrReply(cno));
    }

    @Test
    @DisplayName("전체 댓글 및 답글 삭제 성공")
    void successToRemoveAllCommentOrReply() {
        assertTrue(commentService.removeAllCommentOrReply());
        assertTrue(commentService.getAllActiveCmt().isEmpty());
    }
}