package com.happydiary.service;

import com.happydiary.dao.BoardDaoImpl;
import com.happydiary.dao.UserDaoImpl;
import com.happydiary.dto.BoardDto;
import com.happydiary.dto.PageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final UserDaoImpl userDao;
    private final BoardDaoImpl boardDao;

    // 게시물 개수 확인
    @Override
    public int getNumberOfBoards() {
        int cnt = 0;
        try {
            cnt = boardDao.count();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return cnt;
    }

    // 페이지에 따른 전체 게시물 조회
    // pno: 페이지 번호, size: 한 페이지 당 게시물 개수
    @Override
    public List<BoardDto> getListOfBoards(int pno) {
        List<BoardDto> boards = new ArrayList<>();
        PageRequestDto pageRequestDto = new PageRequestDto(pno, 10);
        try {
            if (pno > 0) {
                boards = boardDao.selectAll(pageRequestDto);
            } else {
                throw new IllegalArgumentException("Page number must be greater than 0");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return boards;
    }

    // 특정 게시물 조회
    @Override
    public BoardDto getBoard(int bno) {
        BoardDto boardDto = new BoardDto();
        // 잘못된 bno 들어오면 IllegalArgumentException 예외처리
        try {
            if (bno > 0) {
                boardDto = boardDao.select(bno);
            } else {
                throw new IllegalArgumentException("Board number must be greater than 0");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return boardDto;
    }

    // 게시물 검색

    // 1. Option (Title or Writer) 기준

    // 1.1. Option (Title or Writer) 기준으로 검색된 게시물 개수
    // Overload (오버로딩) 사용 - 매개변수 개수 다르게 지정
    @Override
    public int getNumberOfFoundBoards(String option, String keyword) {
        int total = 0;
        // Option 잘못 설정된 경우, IllegalArgumentException 예외처리
        try {
            if (option.equals("title") || option.equals("writer") || option.equals("title_or_writer")) {
                total = boardDao.countSelectedRow(option, keyword);
            } else {
                throw new IllegalArgumentException("This is incorrect option");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return total;
    }

    // 1.2. Option (Title or Writer) 에 따라 조회된 게시물 목록
    @Override
    public List<BoardDto> findByTitleOrWriter(int pno, String option, String keyword) {
        List<BoardDto> foundBoards = new ArrayList<>();
        PageRequestDto pageRequestDto = new PageRequestDto(pno, 10);
        try {
            if (pno > 0
                && (option.equals("title") || option.equals("writer") || option.equals("title_or_writer"))
                && !keyword.isEmpty() && !keyword.isBlank()) {
                foundBoards = boardDao.selectByTitleOrWriter(pageRequestDto, option, keyword);
            } else {
                throw new IllegalArgumentException("This is incorrect pno or option or keyword.");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return foundBoards;
    }

    // 2. 공개여부(Public or Private) 기준

    // 2.1. 공개여부(Public or Private) 기준에 따라 검색된 게시물 개수
    @Override
    public int getNumberOfFoundBoardsByVisibility(String visibility, String id) {
        int total = 0;
        try {
            if (visibility.equals("public") || visibility.equals("private")) {
                total = boardDao.countSelectedRowByVisibleScope(visibility, id);
            } else {
                throw new IllegalArgumentException("This is incorrect visibility");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return total;
    }

    // 2.2. 공개여부(Public or Private) 기준으로 조회된 게시물 목록
    // 2.2.1. Private 의 경우, 로그인한 아이디로 조회하여 해당 사용자가 작성한 게시물만 출력
    @Override
    public List<BoardDto> findByVisibleScope(int pno, String visibility, String id) {
        List<BoardDto> foundBoards = new ArrayList<>();
        try {
            if (visibility.equals("public") || visibility.equals("private")) {
                PageRequestDto pageRequestDto = new PageRequestDto(pno, 10);
                foundBoards = boardDao.selectByVisibleScope(pageRequestDto, visibility, id);
            } else {
                throw new IllegalArgumentException("This is incorrect visibility");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return foundBoards;
    }

    // 3. 칭찬 대상(praise_target) 기준

    // 3.1. 칭찬 대상(praise_target) 기준으로 검색된 게시물 개수
    @Override
    public int getNumberOfFoundBoardsByTarget(String praise_target) {
        int result = 0;
        try {
            if (!praise_target.isEmpty() && !praise_target.isBlank()) {
                result = boardDao.countSelectedRowByTarget(praise_target);
            } else {
                throw new IllegalArgumentException("This is incorrect praise_target");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    // 3.2. 칭찬 대상(praise_target) 기준으로 조회된 게시물 목록
    @Override
    public List<BoardDto> findByPraiseTarget(int pno, String praise_target) {
        List<BoardDto> foundBoards = new ArrayList<>();
        try {
            if (pno > 0 && !praise_target.isEmpty() && !praise_target.isBlank()) {
                PageRequestDto pageRequestDto = new PageRequestDto(pno, 10);
                foundBoards = boardDao.selectByTarget(pageRequestDto, praise_target);
            } else {
                throw new IllegalArgumentException("This is incorrect page number or praise_target");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return foundBoards;
    }

    // 게시물 작성
    @Override
    public boolean makeBoard(BoardDto boardDto) {
        boolean isSuccess = true;
        try {
            int result = boardDao.insert(boardDto);
            if (result != 1) {
                isSuccess = false;
            }
        } catch (Exception e) {
            isSuccess = false;
            e.getMessage();
            e.printStackTrace();
        }
        return isSuccess;
    }

    // 게시물 수정
    @Override
    public boolean modifyBoard(BoardDto boardDto) {
        boolean isSuccess = true;
        try {
            if (boardDao.update(boardDto) != 1) {
                isSuccess = false;
            }
        } catch (Exception e) {
            isSuccess = false;
            e.getMessage();
            e.printStackTrace();
        }
        return isSuccess;
    }

    // 특정 게시물 삭제
    @Override
    public boolean removeBoard(int bno) {
        boolean isSuccess = true;
        try {
            if (bno > 0 && boardDao.select(bno) != null) {
                boardDao.delete(bno);
            } else {
                throw new IllegalArgumentException("Board number must be greater than 0");
            }
        } catch (Exception e) {
            isSuccess = false;
            e.getMessage();
            e.printStackTrace();
        }
        return isSuccess;
    }

    // 전체 게시물 삭제
    @Override
    public boolean removeAllBoards() {
        boolean isSuccess = true;
        try {
            if (boardDao.deleteAll() == 0) {
                isSuccess = false;
            }
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            e.getMessage();
        }
        return isSuccess;
    }
}
