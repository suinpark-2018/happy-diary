package com.happydiary.service;

import com.happydiary.dto.BoardDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
class BoardServiceImplTest {
    private BoardServiceImpl boardService;

    @Autowired
    public BoardServiceImplTest(BoardServiceImpl boardService) {
        this.boardService = boardService;
    }

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 50; i++) {
            BoardDto testDto = new BoardDto("title" + i, "content" + i, null, "writer" + i, "target" + i, "N", "N");
            boardService.makeBoard(testDto);
        }
        for (int i = 51; i <= 100; i++) {
            BoardDto testDto = new BoardDto("title" + i, "content" + i, null, "writer" + i, "target" + i, "Y", "N");
            boardService.makeBoard(testDto);
        }
    }

    @Test
    void setUpDB() {
        for (int i = 1; i <= 20; i++) {
            BoardDto testDto = new BoardDto("title" + i, "content" + i, null, "user" + i, "target" + i, "N", "N");
            boardService.makeBoard(testDto);
        }
        for (int i = 21; i <= 40; i++) {
            BoardDto testDto = new BoardDto("title" + i, "content" + i, null, "user" + i, "target" + i, "Y", "N");
            boardService.makeBoard(testDto);
        }
        for (int i = 41; i <= 70; i++) {
            BoardDto testDto = new BoardDto("title" + i, "content" + i, null, "user40", "target" + i, "N", "N");
            boardService.makeBoard(testDto);
        }
        for (int i = 71; i <= 100; i++) {
            BoardDto testDto = new BoardDto("title" + i, "content" + i, null, "user1", "target" + i, "N", "N");
            boardService.makeBoard(testDto);
        }
    }

    @AfterEach
    void cleanDB() {
        boardService.removeAllBoards();
    }

    @Test
    @DisplayName("BoardService 주입 성공")
    void successToDIOfBoardService() {
        assertNotNull(boardService);
    }

    @Test
    @DisplayName("전체 게시물 개수 확인 성공")
    void successToGetNumberOfBoards() {
        // 더미데이터 저장 현황 확인
        int bfCnt = 100;
        assertEquals(bfCnt, boardService.getNumberOfBoards());

        // 게시물 1개 추가 후 전체 게시물 개수 확인
        BoardDto testDto = new BoardDto("test_title", "test_content", null, "test_writer", "test_target", "Y", "N");
        assertTrue(boardService.makeBoard(testDto));
        assertEquals(bfCnt + 1, boardService.getNumberOfBoards());
    }

    @Test
    @DisplayName("전체 게시물 개수 확인 실패")
    void FailToGetNumberOfBoards() {
        // 더미데이터 저장 현황 확인
        int bfCnt = 100;
        assertEquals(bfCnt, boardService.getNumberOfBoards());

        // 게시물 추가 실패 시
        BoardDto testDto1 = new BoardDto();
        assertFalse(boardService.makeBoard(testDto1));
        assertNotEquals(bfCnt + 1, boardService.getNumberOfBoards());

        // 잘못된 예상 결과
        // 게시물 1개 추가했으나  전체 게시물 개수 10개 추가된 것으로 잘못 예상한 경우
        BoardDto testDto2 = new BoardDto("test_title", "test_content", null, "test_writer", "test_target", "Y", "N");
        assertTrue(boardService.makeBoard(testDto2));
        assertNotEquals(bfCnt + 10, boardService.getNumberOfBoards());
    }

    @Test
    @DisplayName("게시물 작성 성공")
    void successToMakeBoard() {
        // 게시물 1개 작성 성공 여부 확인
        BoardDto testDto1 = new BoardDto("test_title1", "test_content", null, "test_writer", "test_target", "Y", "N");
        assertTrue(boardService.makeBoard(testDto1));
        assertEquals(101, boardService.getNumberOfBoards());

        // 게시물 10개 작성 성공 여부 확인
        for (int i = 101; i <= 110; i++) {
            BoardDto testDto2 = new BoardDto("title" + i, "content" + i, null, "writer" + i, "target" + i, "N", "N");
            assertTrue(boardService.makeBoard(testDto2));
        }
        assertEquals(111, boardService.getNumberOfBoards());

        // 게시물 100개 작성 성공 여부 확인
        for (int i = 111; i <= 210; i++) {
            BoardDto testDto3 = new BoardDto("title" + i, "content" + i, null, "writer" + i, "target" + i, "N", "N");
            assertTrue(boardService.makeBoard(testDto3));
        }
        assertEquals(211, boardService.getNumberOfBoards());

        // 게시물 1000개 작성 성공 여부 확인
        for (int i = 211; i <= 1210; i++) {
            BoardDto testDto3 = new BoardDto("title" + i, "content" + i, null, "writer" + i, "target" + i, "N", "N");
            assertTrue(boardService.makeBoard(testDto3));
        }
        assertEquals(1211, boardService.getNumberOfBoards());
    }

    @Test
    @DisplayName("게시물 작성 실패")
    void failToMakeBoard() {
        // 게시물 정보 입력하지 않은 경우
        BoardDto testDto1 = new BoardDto();
        assertFalse(boardService.makeBoard(testDto1));

        // 필수 입력 항목(ex.title)을 입력 누락한 경우
        BoardDto testDto2 = new BoardDto(null, "content", null, "writer", "target", "Y", "N");
        assertFalse(boardService.makeBoard(testDto2));

        // 지정된 컬럼 크기를 벗어난 값을 입력한 경우
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("Please be aware that an exception will be thrown if the input exceeds the column size.");
        }
        String overLengthTitle = sb.toString();
        assertTrue(overLengthTitle.length() > 50);

        BoardDto testDto3 = new BoardDto(overLengthTitle, "content", null, "writer", "target", "Y", "N");
        assertFalse(boardService.makeBoard(testDto3));
    }

    @Test
    @DisplayName("게시물 전체 삭제 성공")
    void successToRemoveAllBoards() {
        // 우선 더미데이터 100개 정상적으로 저장되어 있음을 확인
        assertTrue(boardService.getNumberOfBoards() > 0);
        // 전체 row 삭제
        assertTrue(boardService.removeAllBoards());
        // 저장된 데이터 없음을 확인
        assertEquals(0, boardService.getNumberOfBoards());
    }

    @Test
    @DisplayName("게시물 전체 삭제 실패")
    void failToRemoveAllBoards() {
        // 삭제할 데이터 없는 경우
        // 전체 데이터 삭제 후에 다시 데이터 삭제 시도 시 실패함을 확인
        assertTrue(boardService.getNumberOfBoards() > 0);
        assertTrue(boardService.removeAllBoards());
        assertEquals(0, boardService.getNumberOfBoards());
        assertFalse(boardService.removeAllBoards());
    }

    @Test
    @DisplayName("특정 게시물 삭제 성공")
    void successToRemoveBoard() {
        // 10 페이지에서 조회된 10개의 게시물 목록에서 가장 최근 게시물 삭제
        List<BoardDto> bfBoards = boardService.getListOfBoards(10);
        assertEquals(10, bfBoards.size());

        int bno = bfBoards.get(0).getBno();
        assertTrue(boardService.removeBoard(bno));

        // 다시 10 페이지 조회 시 1개 삭제되어 9개의 게시물 조회됨
        List<BoardDto> afBoards = boardService.getListOfBoards(10);
        assertEquals(9, afBoards.size());
    }

    @Test
    @DisplayName("특정 게시물 삭제 실패")
    void failToRemoveBoard() {
        // 잘못된 bno 설정한 경우
        int wrongBno = 0;
        assertFalse(boardService.removeBoard(wrongBno));

        // 삭제할 게시물 없는 경우
        int correctBno = boardService.getListOfBoards(1).get(0).getBno();
        assertTrue(boardService.removeAllBoards());
        assertFalse(boardService.removeBoard(correctBno));
    }

    @Test
    @DisplayName("특정 페이지 내에서 게시물 목록 조회 성공")
    void successToGetListOfBoards() {
        // 100개의 게시물을 한 페이지 당 10개 씩 출력
        // 1페이지에 게시물 10개 조회되는지 확인
        // 가장 마지막으로 추가한 게시물 제목: title100
        int pno = 1;
        List<BoardDto> boards = boardService.getListOfBoards(pno);

        assertFalse(boards.isEmpty());
        assertEquals(10, boards.size());
        assertEquals("title100", boards.get(0).getTitle());
    }

    @Test
    @DisplayName("특정 페이지 내에서 게시물 목록 조회 실패")
    void failToGetListOfBoards() {
        // 유효하지 않은 페이지로 조회 시
        int wrongPno = 0;
        assertTrue(boardService.getListOfBoards(wrongPno).isEmpty());

        // 게시물 100개 중 10개 삭제 후 10 페이지 조회 시
        // 10 페이지 존재하지 않게됨에 따라 빈(empty) 리스트 반환
        int correctPno = 10;
        List<BoardDto> correctBoards = boardService.getListOfBoards(correctPno);
        for (int i = 0; i < 10; i++) {
            int bno = correctBoards.get(i).getBno();
            assertTrue(boardService.removeBoard(bno));
        }
        int afPno = 10;
        assertTrue(boardService.getListOfBoards(afPno).isEmpty());
    }

    @Test
    @DisplayName("특정 게시물 조회 성공")
    void successToGetBoard() {
        // 1 페이지의 게시물 목록 중 첫번째 게시물 정보 확인
        // title100, content100, writer100 이 맞는지 확인
        List<BoardDto> boards = boardService.getListOfBoards(1);
        int bno = boards.get(0).getBno();
        BoardDto board = boardService.getBoard(bno);

        String title = board.getTitle();
        assertEquals("title100", title);

        String content = board.getContent();
        assertEquals("content100", content);

        String writer = board.getWriter();
        assertEquals("writer100", writer);
    }

    @Test
    @DisplayName("특정 게시물 조회 실패")
    void failToGetBoard() {
        // 잘못된 게시물 번호(bno)로 조회 시
        int wrongBno = 0;
        assertNull(boardService.getBoard(wrongBno).getBno());
        assertNull(boardService.getBoard(wrongBno).getTitle());
        assertNull(boardService.getBoard(wrongBno).getContent());
    }

    @Test
    @DisplayName("제목/작성자 기준으로 검색된 게시물 개수 확인 성공")
    void successToGetNumberOfFoundBoards() {
        // 1을 포함하는 title or writer 조회 결과 존재함을 확인
        String titleOption = "title";
        String writerOption = "writer";
        String keyword = "1";

        assertTrue(boardService.getNumberOfFoundBoards(titleOption, keyword) > 0);
        assertTrue(boardService.getNumberOfFoundBoards(writerOption, keyword) > 0);
    }

    @Test
    @DisplayName("제목/작성자 기준으로 검색된 게시물 개수 확인 실패")
    void failToGetNumberOfFoundBoards() {
        // 존재하지 않는 키워드로 검색 시
        String titleOption = "title";
        String writerOption = "writer";
        String wrongKeyword = "wrong_keyword";

        assertEquals(0, boardService.getNumberOfFoundBoards(titleOption, wrongKeyword));
        assertEquals(0, boardService.getNumberOfFoundBoards(writerOption, wrongKeyword));

        // Option 명칭 잘못 설정된 경우
        String wrongTitleOption = "wrong_title";
        String wrongWriterOption = "wrong_writer";
        String keyword = "1";

        assertEquals(0, boardService.getNumberOfFoundBoards(wrongTitleOption, keyword));
        assertEquals(0, boardService.getNumberOfFoundBoards(wrongWriterOption, keyword));
    }

    @Test
    @DisplayName("제목/작성자 기준으로 게시물 조회 성공")
    void successToFindByTitleOrWriter() {
        int pno = 1; // 1 페이지
        String titleOption = "title";
        String writerOption = "writer";
        String keyword = "1"; // 1을 포함하는 결과

        assertFalse(boardService.findByTitleOrWriter(pno, titleOption, keyword).isEmpty());
        assertFalse(boardService.findByTitleOrWriter(pno, writerOption, keyword).isEmpty());
    }

    @Test
    @DisplayName("제목/작성자 기준으로 게시물 조회 실패")
    void failToFindByTitleOrWriter() {
        int pno = 1; // 1 페이지
        String titleOption = "title";
        String writerOption = "writer";
        String correctKeyword = "1";

        // 잘못된 option 으로 조회 시
        String wrongTitleOption = "wrong_title";
        String wrongWriterOption = "wrong_writer";
        assertTrue(boardService.findByTitleOrWriter(pno, wrongTitleOption, correctKeyword).isEmpty());
        assertTrue(boardService.findByTitleOrWriter(pno, wrongWriterOption, correctKeyword).isEmpty());

        // 잘못된 keyword 으로 조회 시
        String wrongKeyword = ""; // 빈문자열
        assertTrue(boardService.findByTitleOrWriter(pno, titleOption, wrongKeyword).isEmpty());
        assertTrue(boardService.findByTitleOrWriter(pno, writerOption, wrongKeyword).isEmpty());

        String notExistKeyword = "incorrect_keyword"; // 존재하지 않는 키워드
        assertTrue(boardService.findByTitleOrWriter(pno, titleOption, notExistKeyword).isEmpty());
        assertTrue(boardService.findByTitleOrWriter(pno, writerOption, notExistKeyword).isEmpty());
    }

    @Test
    @DisplayName("공개여부를 기준으로 조회된 게시물 개수 확인 성공")
    void successToGetNumberOfFoundBoards_byVisibleScope() {
        String openedVisibility = "public";
        String closedVisibility = "private";
        String id = "writer1";
        assertTrue(boardService.getNumberOfFoundBoardsByVisibility(openedVisibility, id) > 0);
        assertTrue(boardService.getNumberOfFoundBoardsByVisibility(closedVisibility, id) > 0);
    }

    @Test
    @DisplayName("공개여부를 기준으로 조회된 게시물 개수 확인 실패")
    void failToGetNumberOfFoundBoards_byVisibleScope() {
        String wrongVisibility = "wrong_visibility";
        String id = "writer1";
        assertEquals(0, boardService.getNumberOfFoundBoardsByVisibility(wrongVisibility, id));
    }

    @Test
    @DisplayName("공개여부를 기준으로 게시물 목록 조회 성공")
    void successToFindByVisibleScope() {
        int firstPno = 1; // 1 페이지
        int lastPno = 1; // 1 페이지
        String openedVisibility = "public";
        String closedVisibility = "private";
        String id = "writer1";

        assertFalse(boardService.findByVisibleScope(firstPno, openedVisibility, id).isEmpty());
        assertFalse(boardService.findByVisibleScope(lastPno, closedVisibility, id).isEmpty());
    }

    @Test
    @DisplayName("공개여부를 기준으로 게시물 목록 조회 실패")
    void failToFindByVisibleScope() {
        int correctPno = 1;
        String openedVisibility = "public";
        String closedVisibility = "private";
        String id = "writer1";

        // 잘못된 페이지 설정 시
        int wrongPno = 0;
        assertTrue(boardService.findByVisibleScope(wrongPno, openedVisibility, id).isEmpty());
        assertTrue(boardService.findByVisibleScope(wrongPno, closedVisibility, id).isEmpty());

        // 잘못된 공개여부 설정 시
        String wrongVisibility = "wrong_visibility";
        assertTrue(boardService.findByVisibleScope(correctPno, wrongVisibility, id).isEmpty());
    }

    @Test
    @DisplayName("칭찬 대상을 기준으로 조회된 게시물 개수 확인 성공")
    void successToGetNumberOfFoundBoardsByTarget() {
        String praise_target = "target1"; // target1 문자가 포함된 경우
        assertTrue(boardService.getNumberOfFoundBoardsByTarget(praise_target) > 0);
    }

    @Test
    @DisplayName("칭찬 대상을 기준으로 조회된 게시물 개수 확인 실패")
    void failToGetNumberOfFoundBoardsByTarget() {
        // 존재하지 않는 키워드로 조회한 경우
        String not_exist_praise_target = "not_exist_praise_target";
        assertEquals(0, boardService.getNumberOfFoundBoardsByTarget(not_exist_praise_target));

        // 아무것도 입력 안하고 조회 시도한 경우
        String empty_praise_target = "";
        assertEquals(0, boardService.getNumberOfFoundBoardsByTarget(empty_praise_target));
    }

    @Test
    @DisplayName("칭찬 대상을 기준으로 게시물 조회 성공")
    void successToFindByPraiseTarget() {
        int pno = 1; // 1 페이지
        String praise_target = "target1"; // target1 문자가 포함된 경우
        assertFalse(boardService.findByPraiseTarget(pno, praise_target).isEmpty());
    }

    @Test
    @DisplayName("칭찬 대상을 기준으로 게시물 조회 실패")
    void failToFindByPraiseTarget() {
        int pno = 1;
        String praise_target = "target1"; // target1 문자가 포함된 경우

        // 잘못된 페이지로 조회 시
        int wrongPno = 0;
        assertTrue(boardService.findByPraiseTarget(wrongPno, praise_target).isEmpty());

        // 잘못된 키워드로 조회 시
        String wrongTarget = "wrong_praise_target";
        assertTrue(boardService.findByPraiseTarget(pno, wrongTarget).isEmpty());
    }

    @Test
    @DisplayName("게시물 수정 성공")
    void successToModifyBoard() {
        // 임의의 게시물 조회
        BoardDto testDto = boardService.getListOfBoards(1).get(0); // 1페이지의 첫번째 게시물 조회
        assertEquals("title100", testDto.getTitle());

        // 변경하기 위한 정보
        String modifiedTitle = "modified_title";
        testDto.setTitle(modifiedTitle);

        // 변경
        assertTrue(boardService.modifyBoard(testDto));

        // 변경여부 확인
        BoardDto modifiedDto = boardService.getListOfBoards(1).get(0);
        assertEquals(modifiedTitle, modifiedDto.getTitle());
    }

    @Test
    @DisplayName("게시물 수정 실패")
    void failToModifyBoard() {
        // 임의의 게시물 조회
        BoardDto testDto = boardService.getListOfBoards(1).get(0); // 1페이지의 첫번째 게시물 조회
        assertEquals("title100", testDto.getTitle());

        // 지정된 컬럼 크기를 벗어난 값을 입력한 경우
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("Please be aware that an exception will be thrown if the input exceeds the column size.");
        }
        String overLengthTitle = sb.toString();
        assertTrue(overLengthTitle.length() > 50);

        // 초과된 컬럼 길이를 가진 문자열로 title 값 변경 후
        // 게시물 제목 변경 시 실패함
        testDto.setTitle(overLengthTitle);
        assertFalse(boardService.modifyBoard(testDto));
    }
}