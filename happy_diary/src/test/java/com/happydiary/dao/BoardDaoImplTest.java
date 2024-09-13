package com.happydiary.dao;

import com.happydiary.dto.BoardDto;
import com.happydiary.dto.PageRequestDto;
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
class BoardDaoImplTest {
    @Mock BoardDaoImpl mockDao;
    @Autowired BoardDaoImpl boardDao;

    // Mock 객체 초기화
    @BeforeEach
    void setUpMock() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    @DisplayName("게시물 저장 성공")
    void setDB() throws Exception {
        for (int i = 1; i <= 50; i++) {
            BoardDto testDto = new BoardDto("title" + i, "content" + i, null, "writer" + i, "target" + i, "N", "N");
            assertEquals(1, boardDao.insert(testDto));
        }
        for (int i = 51; i <= 100; i++) {
            BoardDto testDto = new BoardDto("title" + i, "content" + i, null, "writer" + i, "target" + i, "Y", "N");
            assertEquals(1, boardDao.insert(testDto));
        }
    }

    @AfterEach
    @DisplayName("게시물 전체 삭제 성공")
    void cleanDB() throws Exception {
        int cnt = boardDao.count();
        assertEquals(cnt, boardDao.deleteAll());
    }

    @Test
    @DisplayName("BoardDao 객체 주입 성공")
    void successToBoardDaoDI() {
        assertNotNull(boardDao);
    }

    @Test
    @DisplayName("게시물 목록 개수 확인 성공")
    void successToCount() throws Exception {
        int bfCnt = boardDao.count();

        BoardDto testDto = new BoardDto("new_title", "new_content", null, "new_writer", "new_target", "Y", "N");
        assertEquals(1, boardDao.insert(testDto));
        int afCnt = bfCnt + 1;

        assertEquals(afCnt, boardDao.count());
    }

    @Test
    @DisplayName("게시물 목록 개수 확인 실패")
    void failToCount() throws Exception {
        int result = boardDao.count();
        assertNotEquals(101, result);
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 게시물 목록 개수 확인 실패")
    void failToDBConnection_count() throws Exception {
        // count 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.count()).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.count());
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 게시물 목록 개수 확인 실패")
    void failToServerConnection_count() throws Exception {
        // 특정 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.count()).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.count());
    }

    @Test
    @DisplayName("게시물 작성 성공")
    void successToInsert() throws Exception {
        // 1개 추가 성공
        BoardDto testDto1 = new BoardDto("new_title1", "new_content", null, "new_writer", "new_target", "Y", "N");
        assertEquals(1, boardDao.insert(testDto1));

        // 10개 추가 성공
        for (int i = 1; i <= 10; i++) {
            BoardDto testDto2 = new BoardDto("new_title2" + i, "new_content" + i, null, "new_writer" + i, "new_target" + i, "N", "N");
            assertEquals(1, boardDao.insert(testDto2));
        }

        // 100개 추가 성공
        for (int i = 1; i <= 100; i++) {
            BoardDto testDto2 = new BoardDto("new_title3" + i, "new_content" + i, null, "new_writer" + i, "new_target" + i, "N", "N");
            assertEquals(1, boardDao.insert(testDto2));
        }

        assertEquals(211, boardDao.count());
    }

    @Test
    @DisplayName("게시물 작성 실패_컬럼 길이 초과")
    void failToInsert_overLength() {
        // title 문자열 길이를 50보다 크게 설정
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("title 타입 길이 VARCHAR(50)으로 설정되어 있으나, 해당 길이를 초과하는 문자열을 저장하려고 했을 때의 결과");
        }

        assertTrue(sb.length() > 50);
        String overLengthTitle = sb.toString();

        BoardDto testDto = new BoardDto(overLengthTitle, "new_content", null, "new_writer", "new_target", "Y", "N");
        assertThrows(DataIntegrityViolationException.class, () -> boardDao.insert(testDto));
    }

    @Test
    @DisplayName("게시물 작성 실패_필수항목 입력 누락")
    void failToInsert_null() {
        String emptyTitle = null;
        BoardDto testDto = new BoardDto(emptyTitle, "new_content", null, "new_writer", "new_target", "Y", "N");
        assertThrows(DataIntegrityViolationException.class, () -> boardDao.insert(testDto));
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 게시물 작성 실패")
    void failToDBConnection_insert() throws Exception {
        BoardDto testDto = new BoardDto("new_title", "new_content", null, "new_writer", "new_target", "Y", "N");

        // count 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.insert(testDto)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.insert(testDto));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 게시물 게시물 작성 실패")
    void failToServerConnection_insert() throws Exception {
        BoardDto testDto = new BoardDto("new_title", "new_content", null, "new_writer", "new_target", "Y", "N");

        // 특정 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.insert(testDto)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.insert(testDto));
    }

    @Test
    @DisplayName("페이징 처리 성공")
    void successToPaging() throws Exception {
        // 첫번째 게시물부터 10개의 게시물 조회 가능
        // 아래와 같은 2가지 방법으로 페이지 번호와 출력할 게시물 개수를 설정할 수 있음
        // PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(1).size(10).build();

        // console 창에서 출력값(게시물 목록) 확인 가능
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        for(BoardDto board : boards) {
            System.out.println(board);
        }
    }

    @Test
    @DisplayName("특정 페이지 내 게시물 목록 조회 성공")
    void successToSelectAll() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        assertFalse(boards.isEmpty());
        assertEquals(10, boards.size());
    }

    @Test
    @DisplayName("페이지 번호 설정 실패로 인한 특정 페이지 내 게시물 목록 조회 실패")
    void failToSelectAll() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(11, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        assertTrue(boards.isEmpty());
    }

    @Test
    @DisplayName("제목, 작성자로 검색된 게시물 개수 확인 성공")
    void successToCountSelectedRow() throws Exception {
        String option1 = "title";
        String option2 = "writer";
        String keyword = "1";
        assertTrue(boardDao.countSelectedRow(option1, keyword) > 0);
        assertTrue(boardDao.countSelectedRow(option2, keyword) > 0);
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 제목, 작성자로 검색된 게시물 개수 확인 실패")
    void failToDBConnection_countSelectedRow() throws Exception {
        String option = "title";
        String keyword = "1";

        // 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.countSelectedRow(option, keyword)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.countSelectedRow(option, keyword));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 제목, 작성자로 검색된 게시물 개수 확인 실패")
    void failToServerConnection_countSelectedRow() throws Exception {
        String option = "title";
        String keyword = "1";

        // 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.countSelectedRow(option, keyword)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.countSelectedRow(option, keyword));
    }

    @Test
    @DisplayName("제목, 작성자로 게시물 검색 성공")
    void successToSelectByTitleOrWriter() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String option1 = "title";
        String keyword1 = "1";

        List<BoardDto> boards1 = boardDao.selectByTitleOrWriter(pageRequestDto, option1, keyword1);
        assertNotNull(boards1);

        String option2 = "writer";
        String keyword2 = "1";

        List<BoardDto> boards2 = boardDao.selectByTitleOrWriter(pageRequestDto, option2, keyword2);
        assertNotNull(boards2);

    }

    @Test
    @DisplayName("제목, 작성자로 게시물 검색 실패")
    void failToSelectByTitleOrWriter() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);

        String option1 = "title";
        String keyword1 = "wrong_title";

        List<BoardDto> boards1 = boardDao.selectByTitleOrWriter(pageRequestDto, option1, keyword1);
        assertTrue(boards1.isEmpty());

        String option2 = "writer";
        String keyword2 = "wrong_writer";

        List<BoardDto> boards2 = boardDao.selectByTitleOrWriter(pageRequestDto, option2, keyword2);
        assertTrue(boards2.isEmpty());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 제목, 작성자로 게시물 검색 실패")
    void failToDBConnection_selectByTitleOrWriter() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String option = "title";
        String keyword = "1";

        // 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.selectByTitleOrWriter(pageRequestDto, option, keyword)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.selectByTitleOrWriter(pageRequestDto, option, keyword));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 제목, 작성자로 게시물 검색 실패")
    void failToServerConnection_selectByTitleOrWriter() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String option = "writer";
        String keyword = "1";

        // 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.selectByTitleOrWriter(pageRequestDto, option, keyword)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.selectByTitleOrWriter(pageRequestDto, option, keyword));
    }

    @Test
    @DisplayName("공개여부로 검색한 게시물 개수 확인 성공")
    void successToCountSelectedRowByVisibleScope() throws Exception {
        String id = "writer1";

        String visibility1 = "public";
        int publicBoardCnt = boardDao.countSelectedRowByVisibleScope(visibility1, id);
        assertTrue(publicBoardCnt > 0);
        assertEquals(50, publicBoardCnt);

        String visibility2 = "private";
        int privateBoardCnt = boardDao.countSelectedRowByVisibleScope(visibility2, id);
        assertTrue(privateBoardCnt > 0);
        assertEquals(1, privateBoardCnt);
    }

    @Test
    @DisplayName("공개여부로 검색한 게시물 개수 확인 실패")
    void failToCountSelectedRowByVisibleScope() throws Exception {
        String id = "writer1";

        String visibility1 = "public";
        int publicBoardCnt = boardDao.countSelectedRowByVisibleScope(visibility1, id);
        assertNotEquals(1, publicBoardCnt);

        String visibility2 = "private";
        int privateBoardCnt = boardDao.countSelectedRowByVisibleScope(visibility2, id);
        assertNotEquals(50, privateBoardCnt);

        String wrongVisibility = "wrong_visibility";
        int boardCnt = boardDao.countSelectedRowByVisibleScope(wrongVisibility, id);
        assertEquals(0, boardCnt);
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 제목, 작성자로 게시물 검색 실패")
    void failToDBConnection_countSelectedRowByVisibleScope() throws Exception {
        String visibility = "public";
        String id = "writer1";

        // 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.countSelectedRowByVisibleScope(visibility, id)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.countSelectedRowByVisibleScope(visibility, id));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 제목, 작성자로 게시물 검색 실패")
    void failToServerConnection_countSelectedRowByVisibleScope() throws Exception {
        String visibility = "private";
        String id = "writer1";

        // 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.countSelectedRowByVisibleScope(visibility, id)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.countSelectedRowByVisibleScope(visibility, id));
    }

    @Test
    @DisplayName("공개여부로 게시물 조회 성공")
    void successToSelectByVisibleScope() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String id = "writer1";

        String visibility1 = "public";
        List<BoardDto> boards1 = boardDao.selectByVisibleScope(pageRequestDto, visibility1, id);

        assertFalse(boards1.isEmpty());
        assertEquals(10, boards1.size());
        for (BoardDto board : boards1) {
            assertEquals("Y", board.getIs_public());
        }

        String visibility2 = "private";
        List<BoardDto> boards2 = boardDao.selectByVisibleScope(pageRequestDto, visibility2, id);

        assertFalse(boards2.isEmpty());
        assertEquals(1, boards2.size());
        for (BoardDto board : boards2) {
            assertEquals("N", board.getIs_public());
        }
    }

    @Test
    @DisplayName("공개여부로 게시물 조회 실패")
    void failToSelectByVisibleScope() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);

        String visibility = "wrong_visible_scope";
        String id = "writer1";
        List<BoardDto> boards = boardDao.selectByVisibleScope(pageRequestDto, visibility, id);
        assertTrue(boards.isEmpty());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 공개여부로 게시물 조회 실패")
    void failToDBConnection_selectByVisibleScope() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String visibility = "public";
        String id = "writer1";

        // 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.selectByVisibleScope(pageRequestDto, visibility, id)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.selectByVisibleScope(pageRequestDto, visibility, id));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 공개여부로 게시물 조회 실패")
    void failToServerConnection_selectByVisibleScope() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String visibility = "private";
        String id = "writer1";

        // 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.selectByVisibleScope(pageRequestDto, visibility, id)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.selectByVisibleScope(pageRequestDto, visibility, id));
    }

    @Test
    @DisplayName("칭찬 대상으로 조회된 게시물 개수 확인 성공")
    void successToCountSelectedRowByTarget() throws Exception {
        String praise_target = "1";
        assertTrue(boardDao.countSelectedRowByTarget(praise_target) > 0);
    }

    @Test
    @DisplayName("칭찬 대상으로 조회된 게시물 개수 확인 실패")
    void failToCountSelectedRowByTarget() throws Exception {
        // 존재하지 않는 대상 이름으로 조회 시
        String praise_target1 = "wrong_target";
        assertFalse(boardDao.countSelectedRowByTarget(praise_target1) > 0);

        // 존재하는 대상의 이름으로 조회했으나 예상한 결과값이 틀렸을 때
        String praise_target2 = "target";
        int expectedCnt = 1;
        assertNotEquals(expectedCnt, boardDao.countSelectedRowByTarget(praise_target2));
    }

    @Test
    @DisplayName("칭찬 대상으로 게시물 조회 성공")
    void successToSelectByTarget() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String praise_target = "1";
        List<BoardDto> boards = boardDao.selectByTarget(pageRequestDto, praise_target);
        assertFalse(boards.isEmpty());
    }

    @Test
    @DisplayName("칭찬 대상으로 게시물 조회 실패")
    void failToSelectByTarget() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);

        String praise_target1 = "wrong_target";
        List<BoardDto> boards1 = boardDao.selectByTarget(pageRequestDto, praise_target1);

        assertTrue(boards1.isEmpty());

        String praise_target2 = "1";
        int expectedCnt = 1;
        List<BoardDto> boards2 = boardDao.selectByTarget(pageRequestDto, praise_target2);
        assertNotEquals(expectedCnt, boards2.size());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 공개여부로 게시물 조회 실패")
    void failToDBConnection_selectByTarget() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String praise_target = "1";

        // 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.selectByTarget(pageRequestDto, praise_target)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.selectByTarget(pageRequestDto, praise_target));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 공개여부로 게시물 조회 실패")
    void failToServerConnection_selectByTarget() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        String praise_target = "1";

        // 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.selectByTarget(pageRequestDto, praise_target)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.selectByTarget(pageRequestDto, praise_target));
    }

    @Test
    @DisplayName("특정 게시물 조회 성공")
    void successToSelect() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();
        assertEquals("title100", boardDao.select(bno).getTitle());
    }

    @Test
    @DisplayName("특정 게시물 조회 실패")
    void failToSelect() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();
        String expectedTitle1 = "wrong_title";
        assertNotEquals(expectedTitle1, boardDao.select(bno).getTitle());

        int wrongBno = 5;
        String expectedTitle2 = "title100";
        assertNotEquals(expectedTitle2, boardDao.select(wrongBno));
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 게시물 조회 실패")
    void failToDBConnection_select() throws Exception {
        BoardDto testDto = new BoardDto("new_title", "new_content", null, "new_writer", "new_target", "Y", "N");

        // count 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.select(1)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.select(1));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 게시물 게시물 조회 실패")
    void failToServerConnection_select() throws Exception {
        BoardDto testDto = new BoardDto("new_title", "new_content", null, "new_writer", "new_target", "Y", "N");

        // 특정 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.select(1)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.select(1));
    }

    @Test
    @DisplayName("게시물 수정 성공")
    void successToUpdate() throws Exception {
        // 임의의 bno -> title100, content100
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();

        // 수정 전 정보
        BoardDto bfDto = boardDao.select(bno);
        String bf_title = bfDto.getTitle();
        String bf_content = bfDto.getContent();
        String bf_img_path = bfDto.getImg_path();
        String bf_praise_target = bfDto.getPraise_target();
        String bf_is_public = bfDto.getIs_public();

        // 새로운 정보
        String new_title = "new_title";
        String new_content = "new_content";
        String new_img_path = "new_img_path";
        String new_praise_target = "new_praise_target";
        String new_is_public = "N";
        BoardDto testDto = new BoardDto(bno, new_title, new_content, new_img_path, new_praise_target, new_is_public);

        // 수정 전 항목들의 값과 변경할 값을 비교하여 불일치함을 확인
        assertNotEquals(bf_title, new_title);
        assertNotEquals(bf_content, new_content);
        assertNotEquals(bf_img_path, new_img_path);
        assertNotEquals(bf_praise_target, new_praise_target);
        assertNotEquals(bf_is_public, new_is_public);

        // 수정 성공 시 1 반환
        assertEquals(1, boardDao.update(testDto));

        // 예상했던 값과 수정 후 실제 변경된 값이 일치하는지 확인
        BoardDto afDto = boardDao.select(bno);
        assertEquals(new_title, afDto.getTitle());
        assertEquals(new_content, afDto.getContent());
        assertEquals(new_img_path, afDto.getImg_path());
        assertEquals(new_praise_target, afDto.getPraise_target());
        assertEquals(new_is_public, afDto.getIs_public());
    }

    @Test
    @DisplayName("컬럼 길이 초과 시 게시물 수정 실패")
    void failToUpdate_overLength() throws Exception {
        // 임의의 bno -> title100, content100
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();

        // title 문자열 길이를 50보다 크게 설정
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("title 타입 길이 VARCHAR(50)으로 설정되어 있으나, 해당 길이를 초과하는 문자열을 저장하려고 했을 때의 결과");
        }

        assertTrue(sb.length() > 50);
        String overLengthTitle = sb.toString();

        BoardDto testDto = boardDao.select(bno);
        testDto.setTitle(overLengthTitle);

        assertThrows(DataIntegrityViolationException.class, () -> boardDao.update(testDto));
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 게시물 수정 실패")
    void failToDBConnection_update() throws Exception {
        // 임의의 bno -> title100, content100
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();

        BoardDto testDto = boardDao.select(bno);
        testDto.setTitle("updated_title");

        // count 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.update(testDto)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.update(testDto));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 게시물 게시물 수정 실패")
    void failToServerConnection_update() throws Exception {
        // 임의의 bno -> title100, content100
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();

        BoardDto testDto = boardDao.select(bno);
        testDto.setTitle("updated_title");

        // 특정 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.update(testDto)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.update(testDto));
    }

    @Test
    @DisplayName("특정 게시물 삭제 성공")
    void successToDelete() throws Exception {
        // 임의의 bno -> title100, content100
        PageRequestDto pageRequestDto = new PageRequestDto(10, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();

        // 특정 게시물 삭제 전 10페이지에 출력되는 게시물 수 비교
        int bfCnt = boardDao.selectAll(pageRequestDto).size();
        assertEquals(10, bfCnt);

        // 특정 게시물 1개 삭제 후 10페이지에 출력되는 게시물 수 비교
        assertEquals(1, boardDao.delete(bno));
        int afCnt = boardDao.selectAll(pageRequestDto).size();
        assertEquals(9, afCnt);
    }

    @Test
    @DisplayName("특정 게시물 삭제 실패")
    void failToDelete() throws Exception {
        PageRequestDto pageRequestDto = new PageRequestDto(10, 10);
        // 존재하지 않는 bno 으로 삭제 시도 시
        int bno = 0;

        // 특정 게시물 삭제 전 10페이지에 출력되는 게시물 수 비교
        int bfCnt = boardDao.selectAll(pageRequestDto).size();
        assertEquals(10, bfCnt);

        // 특정 게시물 1개 삭제 후 10페이지에 출력되는 게시물 수 비교
        assertEquals(0, boardDao.delete(bno)); // 삭제 실패
        int afCnt = boardDao.selectAll(pageRequestDto).size();
        assertNotEquals(9, afCnt);
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 게시물 수정 실패")
    void failToDBConnection_delete() throws Exception {
        // 임의의 bno
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();

        // count 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.delete(bno)).thenThrow(new DataAccessException("Database connection failed!") {});
        assertThrows(DataAccessException.class, () -> mockDao.delete(bno));
    }

    @Test
    @DisplayName("Server 연결 실패로 인한 게시물 게시물 수정 실패")
    void failToServerConnection_delete() throws Exception {
        // 임의의 bno
        PageRequestDto pageRequestDto = new PageRequestDto(1, 10);
        List<BoardDto> boards = boardDao.selectAll(pageRequestDto);
        int bno = boards.get(0).getBno();

        // 특정 메서드 호출 시 서버 연결 예외 발생시킴
        when(mockDao.delete(bno)).thenThrow(new ConnectException("Server connection failed!"));
        assertThrows(ConnectException.class, () -> mockDao.delete(bno));
    }
}