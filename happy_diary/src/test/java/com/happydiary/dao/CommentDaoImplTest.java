package com.happydiary.dao;

import com.happydiary.dto.CommentDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.ConnectException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
class CommentDaoImplTest {
    @Mock CommentDaoImpl mockDao;
    @Autowired CommentDaoImpl commentDao;

    // Mock 객체 초기화
    @BeforeEach
    void setUpMock() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
//    @Test
    @DisplayName("댓글 추가 성공 및 더미데이터 DB 저장")
    void setDB() throws Exception {
        // 첫번째, 두번째 게시물에 100개의 댓글 추가
        for (int i = 1; i <= 100; i++) {
            CommentDto testDtos1 = new CommentDto(1, null, "writer" + i, "comment" + i);
            assertEquals(1, commentDao.insert(testDtos1));

            CommentDto testDtos2 = new CommentDto(2, null, "writer" + i, "comment" + i);
            assertEquals(1, commentDao.insert(testDtos2));
        }

        // 첫번째, 두번째 게시물의 마지막 댓글에 50개의 대댓글 추가
        List<CommentDto> cmts = commentDao.selectActiveAll();
        int cno1 = cmts.get(0).getCno();
        int cno2 = cmts.get(1).getCno();
        for (int i = 1; i <= 50; i++) {
            CommentDto testDtos1 = new CommentDto(1, cno1, "writer" + i, "comment" + i);
            assertEquals(1, commentDao.insert(testDtos1));

            CommentDto testDtos2 = new CommentDto(1, cno2, "writer" + i, "comment" + i);
            assertEquals(1, commentDao.insert(testDtos2));
        }

        // 댓글 및 대댓글 전체 개수 확인
        assertEquals(300, commentDao.countActiveCmt());
    }

    @AfterEach
    @DisplayName("게시물 전체 삭제 성공")
    void cleanDB() throws Exception {
        int total = commentDao.countAll();
        assertEquals(total, commentDao.deleteAll());
    }

    @Test
    @DisplayName("CommentDao 주입 성공")
    void successToDIOfCommentDao() throws Exception {
        assertNotNull(commentDao);
    }

    @Test
    @DisplayName("댓글 개수 확인 성공")
    void successToCount() throws Exception {
        int totalCnt = commentDao.countActiveCmt();

        CommentDto testDto = new CommentDto(1, null, "new_writer200", "new_comment_content");
        assertEquals(1, commentDao.insert(testDto));

        assertEquals(totalCnt + 1, commentDao.countActiveCmt());
    }

    @Test
    @DisplayName("댓글 개수 확인 실패")
    void failToCount() throws Exception {
        int expectedCnt = 1; // 잘못 예상한 갯수
        assertNotEquals(expectedCnt, commentDao.countActiveCmt());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 댓글 개수 확인 실패")
    void failToDBConnection_count() throws Exception {
        when(mockDao.countActiveCmt()).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.countActiveCmt());
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 댓글 개수 확인 실패")
    void failToServerConnection_count() throws Exception {
        when(mockDao.countActiveCmt()).thenThrow(new ConnectException());
        assertThrows(ConnectException.class, () -> mockDao.countActiveCmt());
    }

    @Test
    @DisplayName("댓글 및 대댓글 추가 성공")
    void successToInsert() throws Exception {
        // 기존의 전체 댓글 수 확인
        int bfCnt = commentDao.countAll();

        // 댓글 1개 추가 후 성공여부 확인
        CommentDto testDto1 = new CommentDto(1, null, "Tester", "test_content");
        assertEquals(1, commentDao.insert(testDto1));

        // 댓글 추가 후 전체 comment 수 증가했는지 확인
        assertEquals(bfCnt + 1, commentDao.countActiveCmt());

        // 대댓글 1개 추가 후 성공여부 확인
        CommentDto testDto2 = new CommentDto(1, 1, "Tester", "test_content");
        assertEquals(1, commentDao.insert(testDto2));

        // 대댓글 추가 후 전체 comment 수 증가했는지 확인
        assertEquals(bfCnt + 2, commentDao.countActiveCmt());
    }

    @Test
    @DisplayName("댓글 및 대댓글 추가 실패_컬럼 길이 초과")
    void failToInsert_overLength() throws Exception {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("writer 타입 길이 VARCHAR(20)으로 설정되어 있으나, 해당 길이를 초과하는 문자열을 저장하려고 했을 때의 결과");
        }
        assertTrue(sb.length() > 50);

        String overLengthWriter = sb.toString();
        CommentDto testDto = new CommentDto(1, null, overLengthWriter, "new_content");
        assertThrows(DataIntegrityViolationException.class, () -> commentDao.insert(testDto));
    }

    @Test
    @DisplayName("댓글 및 대댓글 추가 실패_NOT NULL 항목 누락")
    void failToInsert_null() throws Exception {
        String emptyWriter = null;
        CommentDto testDto = new CommentDto(1, null, emptyWriter, "new_content");
        assertThrows(DataIntegrityViolationException.class, () -> commentDao.insert(testDto));
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 댓글 추가 실패")
    void failToDBConnection_insert() throws Exception {
        CommentDto testDto = new CommentDto(1, null, "new_writer", "new_content");

        // count 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.insert(testDto)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.insert(testDto));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 댓글 추가 실패")
    void failToServerConnection_insert() throws Exception {
        CommentDto testDto = new CommentDto(1, null, "new_writer", "new_content");

        // 특정 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.insert(testDto)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.insert(testDto));
    }

    @Test
    @DisplayName("전체 댓글 및 대댓글 조회 성공")
    void successToSelectActiveCommentsAndReplies() throws Exception {
        int expectedCnt = commentDao.countActiveCmt();
        int actualCnt = commentDao.selectActiveAll().size();

        assertTrue(actualCnt > 0);
        assertEquals(expectedCnt, actualCnt);
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 전체 댓글 조회 실패")
    void failToDBConnection_selectActiveAll() throws Exception {
        when(mockDao.selectActiveAll()).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.selectActiveAll());
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 댓글 조회 실패")
    void failToServerConnection_selectActiveAll() throws Exception {
        when(mockDao.selectActiveAll()).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.selectActiveAll());
    }

    @Test
    @DisplayName("특정 게시물의 댓글 및 대댓글 조회 성공")
    void successToSelectAllCmt() throws Exception {
        int bno = 1;
        int totalCnt = commentDao.selectAllComment(bno).size();
        assertEquals(200, totalCnt);
    }

    @Test
    @DisplayName("존재하지 않는 게시물 번호로 댓글 및 대댓글 조회 시 실패")
    void failToSelectAllCmt_notExistBno() throws Exception {
        int bno = 0;
        assertTrue(commentDao.selectAllComment(bno).isEmpty());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 특정 게시물의 댓글 및 대댓글 조회 실패")
    void failToDBConnection_selectAll() throws Exception {
        when(mockDao.selectAllComment(1)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.selectAllComment(1));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 특정 게시물의 댓글 조회 실패")
    void failToServerConnection_selectAll() throws Exception {
        when(mockDao.selectAllComment(1)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.selectAllComment(1));
    }

    @Test
    @DisplayName("특정 댓글의 답글 조회 성공")
    void successToSelectAllReply() throws Exception {
        List<CommentDto> cmts = commentDao.selectActiveAll();
        int ptCno = cmts.get(0).getParent_cno();

        assertFalse(commentDao.selectAllReply(ptCno).isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 댓글번호로 조회 시 답글 조회 실패")
    void failToSelectAllReply_notExistCmtNo() throws Exception {
        int ptCno = 0;
        assertTrue(commentDao.selectAllReply(ptCno).isEmpty());
    }

    @Test
    @DisplayName("답글 없는 댓글인 경우 답글 조회 실패")
    void failToSelectAllReply_notExistReply() throws Exception {
        // 세번째 댓글에는 답글 없음
        List<CommentDto> cmts = commentDao.selectActiveAll();
        int ptCno = cmts.get(0).getCno();
        assertTrue(commentDao.selectAllReply(ptCno).isEmpty());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 특정 댓글의 답글 조회 실패")
    void failToDBConnection_selectAllReply() throws Exception {
        List<CommentDto> cmts = commentDao.selectActiveAll();
        int ptCno = cmts.get(0).getParent_cno();

        when(mockDao.selectAllReply(ptCno)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.selectAllReply(ptCno));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 특정 댓글의 답글 조회 실패")
    void failToServerConnection_selectAllReply() throws Exception {
        List<CommentDto> cmts = commentDao.selectActiveAll();
        int ptCno = cmts.get(0).getParent_cno();

        when(mockDao.selectAllReply(ptCno)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.selectAllReply(ptCno));
    }

    @Test
    @DisplayName("댓글 및 답글 수정 성공")
    void successToUpdate() throws Exception {
        String modifiedContent = "modified_content";
        int cno = commentDao.selectActiveAll().get(0).getCno();
        CommentDto testDto = commentDao.select(cno);
        assertNotEquals(modifiedContent, testDto.getContent());

        testDto.setContent(modifiedContent);
        assertEquals(1, commentDao.update(testDto));
        assertEquals(modifiedContent, commentDao.select(cno).getContent());
    }

    @Test
    @DisplayName("컬럼 길이 초과로 인한 댓글 및 답글 수정 실패")
    void failToUpdate_overLength() throws Exception {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("content 타입 길이 VARCHAR(100)으로 설정되어 있으나, 해당 길이를 초과하는 문자열을 저장하려고 했을 때의 결과");
        }
        assertTrue(sb.length() > 100);

        String content = sb.toString();
        CommentDto testDto = commentDao.selectActiveAll().get(0);
        testDto.setContent(content);
        assertThrows(DataIntegrityViolationException.class, () -> commentDao.update(testDto));
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 댓글 및 답글 수정 실패")
    void failToDBConnection_update() throws Exception {
        CommentDto testDto = commentDao.selectActiveAll().get(0);
        testDto.setContent("modified_content");

        when(mockDao.update(testDto)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.update(testDto));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 댓글 및 답글 수정 실패")
    void failToServerConnection_update() throws Exception {
        CommentDto testDto = commentDao.selectActiveAll().get(0);
        testDto.setContent("modified_content");

        when(mockDao.update(testDto)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.update(testDto));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void successToDelete() throws Exception {
        CommentDto testDto = commentDao.selectActiveAll().get(0);
        int cno = testDto.getCno();
        assertNotNull(commentDao.select(cno));

        assertEquals(1, commentDao.delete(cno));
        assertNull(commentDao.select(cno));
    }

    @Test
    @DisplayName("존재하지 않는 댓글번호로 인한 댓글 삭제 실패")
    void failToDelete() throws Exception {
        int cno = 0;
        assertEquals(0, commentDao.delete(cno));
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 댓글 삭제 실패")
    void failToDBConnection_delete() throws Exception {
        int cno = commentDao.selectActiveAll().get(0).getCno();

        when(mockDao.delete(cno)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.delete(cno));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 댓글 삭제 실패")
    void failToServerConnection_delete() throws Exception {
        int cno = commentDao.selectActiveAll().get(0).getCno();

        when(mockDao.delete(cno)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.delete(cno));
    }

    @Test
    @DisplayName("데이터 전체 삭제 성공")
    void successToDeleteAll() throws Exception {
        int totalCnt = commentDao.countAll(); // 댓글 + 답글
        assertEquals(300, commentDao.deleteAll());
    }

    @Test
    @DisplayName("삭제할 데이터 없음에 따른 데이터 전체 삭제 실패")
    void failToDeleteAll() throws Exception {
        commentDao.deleteAll();
        assertEquals(0, commentDao.deleteAll());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 댓글 및 답글 전체 삭제 실패")
    void failToDBConnection_deleteAll() throws Exception {
        when(mockDao.deleteAll()).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.deleteAll());
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 댓글 및 답글 전체 삭제 실패")
    void failToServerConnection_deleteAll() throws Exception {
        when(mockDao.deleteAll()).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.deleteAll());
    }
}