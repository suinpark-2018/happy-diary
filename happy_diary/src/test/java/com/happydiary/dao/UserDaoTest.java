package com.happydiary.dao;

import com.happydiary.dto.UserDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
class UserDaoTest {

    @Mock UserDaoImpl mockDao;
    @Autowired UserDaoImpl userDao;

    // Mock 객체 초기화
    @BeforeEach
    void setUpMock() {
        MockitoAnnotations.openMocks(this);
    }

    // 테스트 전 DB 테이블에 더미데이터 추가
    @BeforeEach
    void setUpDB() throws Exception {
        for (int i = 1; i <= 10; i++) {
            UserDto testDto = new UserDto("user" + i, "Password" + i + "!", "name" + i, "user" + i + "@happydiary.com", "010-1234-5678", "19950102", "M", "서울시 강남구 영동대로 1");
            userDao.insert(testDto);
        }
        for (int i = 11; i <= 20; i++) {
            UserDto testDto = new UserDto("user" + i, "Password" + i + "!", "name" + i, "user" + i + "@happydiary.com", "010-1234-5678", "19940208", "F", "서울시 강남구 영동대로 2");
            userDao.insert(testDto);
        }
    }

    // 테스트 종료 후 더미데이터 삭제
    @AfterEach
    void cleanDB() throws Exception {
        userDao.deleteAll();
    }

    @Test
    @DisplayName("UserDao 객체 주입 성공여부 확인")
    void successToDIOfUserDao() {
        assertNotNull(userDao);
    }

    // now 메서드 테스트
    @Test
    @DisplayName("현재 날짜 및 시간 오차범위 내 존재함")
    void withinRangeNow() throws Exception {
        // dao 메서드 사용하여 DB에 저장된 현재 datetime 호출하여 저장
        String now = userDao.now();
        assertNotNull(now);

        // DB에 저장되었던 현재 datetime 값을 String -> LocalDateTime 형식으로 변환
        // 현재 서버의 datetime 결과 저장
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dbDateTime = LocalDateTime.parse(now, formatter);
        LocalDateTime currentDateTime = LocalDateTime.now();

        // DB의 datetime과 서버의 datetime의 오차를 초 단위로 계산
        long secondsDifference = ChronoUnit.SECONDS.between(dbDateTime, currentDateTime);

        // 60초 이내인지 확인
        assertTrue(Math.abs(secondsDifference) <= 60);
    }

    @Test
    @DisplayName("현재 날짜 및 시간 오차범위 벗어남")
    void outOfRangeNow() throws Exception {
        // dao 메서드 사용하여 DB에 저장된 현재 datetime 호출하여 저장
        String now = userDao.now();
        assertNotNull(now);

        // DB에 저장되었던 현재 datetime 값을 String -> LocalDateTime 형식으로 변환
        // 현재 서버의 datetime 결과 저장
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dbDateTime = LocalDateTime.parse(now, formatter);

        // 임의로 서버시간을 2시간 앞당김
        LocalDateTime currentDateTime = LocalDateTime.now().minusHours(2);

        // DB의 datetime과 서버의 datetime의 오차를 초 단위로 계산
        long secondsDifference = ChronoUnit.SECONDS.between(dbDateTime, currentDateTime);

        // 60초 이내가 아닌지 확인
        assertFalse(Math.abs(secondsDifference) <= 60);
    }


    // count 메서드 테스트
    @Test
    @DisplayName("Row 카운트 성공")
    void successToCount() throws Exception {
        int expectedCnt = 20; // 더미데이터에 넣었던 데이터 개수
        int actualCnt = userDao.count(); // 실제 카운팅된 row 수
        assertEquals(expectedCnt, actualCnt);

        // 임의의 user 정보 1개 추가
        UserDto testDto1 = new UserDto("test", "Test1234!!", "name", "test@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 1");
        userDao.insert(testDto1);
        assertEquals(21, userDao.count());

        // 임의의 user 정보 10개 추가
        for (int i = 21; i <= 30; i++) {
            UserDto testDto2 = new UserDto("user" + i, "Password" + i + "!", "name" + i, "user" + i + "@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 3");
            userDao.insert(testDto2);
        }
        assertEquals(31, userDao.count());

        // 임의의 user 정보 100개 추가
        for (int i = 31; i <= 130; i++) {
            UserDto testDto3 = new UserDto("user" + i, "Password" + i + "!", "name" + i, "user" + i + "@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 3");
            userDao.insert(testDto3);
        }
        assertEquals(131, userDao.count());
    }

    @Test
    @DisplayName("Row 카운트 실패")
    void failToCount() throws Exception {
        int expectedCnt = 20; // 더미데이터에 넣었던 데이터 개수
        int actualCnt = userDao.count(); // 실제 카운팅된 row 수
        assertEquals(expectedCnt, actualCnt);
        assertNotEquals(expectedCnt-1, actualCnt);
    }

    // 단위 테스트 (Mock 객체 활용)
    @Test
    @DisplayName("DB 연결 실패로 인한 Row 카운트 실패")
    void failToDBConnection_count() throws Exception {
        // count 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.count()).thenThrow(new DataAccessException("Database connection error!") {});
        assertThrows(DataAccessException.class, () -> mockDao.count());
    }

    // insert 메서드 테스트
    @Test
    @DisplayName("데이터 저장 성공 테스트")
    void successToInsert() throws Exception {
        // 데이터 저장 전 row 카운트 결과 확인
        int preCnt = userDao.count();

        // 임의의 UserDto 객체 생성
        // 데이터 저장 및 not null 확인
        // Case 1: 1개 추가 저장
        UserDto testDto = new UserDto("test_id", "Password1" + "!", "test_name", "test@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 3");

        // insert 성공여부 확인
        assertEquals(1, userDao.insert(testDto));
        // DB에 원하는 값 제대로 저장됐는지 확인
        assertEquals("test_name", userDao.select("test_id").getName());

        // 데이터 저장 후 row 카운트 결과 확인
        // 추가 저장한 데이터 갯수만큼 증가했는지 확인
        int postCnt1 = userDao.count();
        assertEquals(preCnt + 1, postCnt1);

        // Case 2: 50개 추가 저장
        for (int i = 1; i <= 50; i++) {
            UserDto addTestDto = new UserDto("test" + i, "Password" + i + "!", "name" + i, "user" + i + "@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 3");
            userDao.insert(addTestDto);
        }

        // 데이터 저장 후 row 카운트 결과 확인
        // 추가 저장한 데이터 갯수만큼 증가했는지 확인
        int postCnt2 = userDao.count();
        assertEquals(preCnt + 51, postCnt2);
        assertEquals(postCnt1 + 50, postCnt2);
    }

    // 단위 테스트 (Mock 객체 활용)
    @Test
    @DisplayName("DB 연결 실패로 인한 데이터 저장 실패")
    void failToDBConnection_insert() throws Exception {
        // 임의의 UserDto 객체 생성
        UserDto testDto = new UserDto("test", "Password1!", "name", "test@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 3");

        // count 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.insert(testDto)).thenThrow(new DataAccessException("Database connection error!") {});
        assertThrows(DataAccessException.class, () -> mockDao.insert(testDto));
    }

    @Test
    @DisplayName("PK 중복인 경우 데이터 저장 실패 테스트")
    void failToInsert_DuplicatedPK() throws Exception {
        // 이미 테스트에 존재하는 정보가 저장된 테스트용 객체 testDto
        UserDto testDto = new UserDto("user1", "Password1!", "name1", "user1@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 3");
        assertThrows(DuplicateKeyException.class, () -> userDao.insert(testDto));
    }

    @Test
    @DisplayName("NOT NULL로 설정된 항목 저장 누락 시 데이터 저장 실패")
    void failToInsert_NotNullColumn() throws Exception {
        // Not Null 항목 누락된 테스트용 객체 testDto
        UserDto testDto = new UserDto("test", null, "test1", "test1@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 3");
        assertThrows(DataIntegrityViolationException.class, () -> userDao.insert(testDto));
    }

    @Test
    @DisplayName("타입 길이 오류로 인한 데이터 저장 실패")
    void failToInsert_TypeLengthError() throws Exception {
        // 잘못된 타입으로 설정한 항목이 포함된 테스트용 객체 testDto
        // name 컬럼의 타입 길이는 varchar(20)으로 설정되어 있으며, 그 이상의 데이터 저장 시도 시 관련 예외 발생하는지 확인
        UserDto testDto = new UserDto("user1", "Password1!", "name12345name56789name...", "user1@happydiary.com", "010-1234-5678", "19940208", "M", "서울시 강남구 영동대로 3");
        assertThrows(DataIntegrityViolationException.class, () -> userDao.insert(testDto));
    }

    // select 메서드 테스트
    @Test
    @DisplayName("데이터 조회 성공")
    void successOfSelect() throws Exception {
        // 기존에 세팅해둔 데이터로 테스트 진행
        UserDto testDto1 = userDao.select("user1");
        assertNotNull(testDto1);
        // DB 조회결과 상 원하는 값으로 조회된 것이 맞는지 확인
        assertEquals("name1", userDao.select("user1").getName());

        // 40개의 데이터 조회 시도
        for (int i = 1; i <= 20 ; i++) {
            UserDto testDto2 = userDao.select("user" + i);
            // 테스트 결과 확인
            assertNotNull(testDto2); // 데이터 조회 성공 여부 확인
            assertEquals("name" + i, testDto2.getName()); // 원하는 데이터로 조회되는지 확인
        }
    }

    @Test
    @DisplayName("데이터 조회 실패")
    void failToSelect() throws Exception {
        // DB에 저장되지 않은 id로 조회 시
        // 조회되는 데이터 없음
        String wrongId = "wrongId";
        assertNull(userDao.select(wrongId));
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 데이터 조회 실패")
    void failToDBConnection_select() throws Exception {
        // 기존에 셋팅해뒀던 id 활용
        String dbId = "uer1";
        // select 메서드 호출 시 DB 접근 예외 발생시킴
        when(mockDao.select(dbId)).thenThrow(new DataAccessException("Database connection error!") {});
        assertThrows(DataAccessException.class, () -> mockDao.select(dbId));
    }

    // selectAll 메서드 테스트
    @Test
    @DisplayName("전체 데이터 조회 성공")
    void successOfSelectAll() throws Exception {
        // 전체 데이터 조회 여부 확인
        List<UserDto> users = userDao.selectAll();
        assertNotNull(users);

        // 테스트용으로 셋팅한 데이터 row 수 일치여부 확인
        assertEquals(20, userDao.count());

        // 의도한 DB 값으로 조회되는지 확인
        for (UserDto user : users) {
            List<String> ids = new ArrayList<>();
            ids.add(user.getId());
            assertTrue(ids.contains(user.getId()));
        }
    }

    @Test
    @DisplayName("전체 데이터 조회 실패")
    void failToSelectAll() throws Exception {
        // 데이터 비우기
        userDao.deleteAll();

        // 데이터 조회 시 빈문자열([]) 반환
        List<UserDto> users = userDao.selectAll();
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 전체 데이터 조회 실패")
    void failToDBConnection_selectAll() throws Exception {
        // selectAll() 호출 시 DB 접근 예외 발생시킴
        when(mockDao.selectAll()).thenThrow(new DataAccessException("Database connection error!"){});

        // 설정된 예외 발생여부 확인
        assertThrows(DataAccessException.class, () -> mockDao.selectAll());
    }

    // update 메서드 테스트
    @Test
    @DisplayName("DB 연결 실패로 인한 데이터 수정 실패")
    void failToDBConnection_update() throws Exception {
        String testId = "user1";
        String testPhoneNum = "010-3344-5566";
        UserDto testDto = userDao.select(testId);

        assertNotEquals(testPhoneNum, testDto.getPhone_num());
        testDto.setPhone_num(testPhoneNum);
        assertEquals(testPhoneNum, testDto.getPhone_num());

        // update() 호출 시 DB 접근 예외 발생시킴
        when(mockDao.update(testDto)).thenThrow(new DataAccessException("Database connection error!"){});
        // 설정된 예외 발생여부 확인
        assertThrows(DataAccessException.class, () -> mockDao.update(testDto));
    }

    @Test
    @DisplayName("비밀번호 수정 성공")
    void successOfUpdate_pwd() throws Exception {
        // 임의의 테스트용 객체
        String testId = "user1";
        UserDto testDto = userDao.select(testId);

        // 변경할 비밀번호
        String pwd = "ModifiedPassword1!";
        // 비밀번호 수정 전
        assertNotEquals(pwd, userDao.select(testId).getPwd());
        // 비밀번호 수정
        testDto.setPwd(pwd);
        assertEquals(1, userDao.update(testDto)); // 수정 성공여부 확인
        // 비밀번호 수정 후
        assertEquals(pwd, userDao.select(testId).getPwd());
    }

    @Test
    @DisplayName("타입 길이 오류로 인한 비밀번호 수정 실패")
    void failToUpdatePwd_TypeLengthError() throws Exception {
        // 잘못된 타입길이로 설정한 항목이 포함된 테스트용 객체 testDto
        // pwd 컬럼의 타입 길이는 varchar(500)으로 설정되어 있으며, 그 이상의 데이터 저장 시도 시 관련 예외 발생하는지 확인
        String testId = "user1";
        String pwd = "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3" + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3"
                + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3" + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3"
                + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3" + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3"
                + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3" + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3"
                + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3" + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3"
                + "B7$kQzL4x!2Fg8MnRvT#1D@9pVa5sUJ6E*bH^jKPyC0WmZ3";

        assertTrue(pwd.length() > 500);

        UserDto testDto = userDao.select(testId);
        testDto.setPwd(pwd);

        assertThrows(DataIntegrityViolationException.class, () -> userDao.update(testDto));
    }

    @Test
    @DisplayName("휴대폰 번호 수정 성공")
    void successOfUpdate_phoneNum() throws Exception {
        // 임의의 테스트용 객체
        String testId = "user1";
        UserDto testDto = userDao.select(testId);

        // 변경할 휴대폰번호
        String phoneNum = "01012334455";
        // 휴대폰번호 수정 전
        assertNotEquals(phoneNum, userDao.select(testId).getPhone_num());
        // 휴대폰번호 수정
        testDto.setPhone_num(phoneNum);
        assertEquals(1, userDao.update(testDto)); // 수정 성공여부 확인
        // 휴대폰번호 수정 후
        assertEquals(phoneNum, userDao.select(testId).getPhone_num());
    }

    @Test
    @DisplayName("타입 길이 오류로 인한 휴대폰 번호 수정 실패")
    void failToUpdatePhoneNum_TypeLengthError() throws Exception {
        // 잘못된 타입길이로 설정한 항목이 포함된 테스트용 객체 testDto
        // phone_num 컬럼의 타입 길이는 varchar(15)으로 설정되어 있으며, 그 이상의 데이터 저장 시도 시 관련 예외 발생하는지 확인
        String testId = "user1";
        String testPhoneNum = "010-1234-1234-5678";
        UserDto testDto = userDao.select(testId);
        testDto.setPhone_num(testPhoneNum);

        assertThrows(DataIntegrityViolationException.class, () -> userDao.update(testDto));
    }

    @Test
    @DisplayName("이메일 수정 성공")
    void successOfUpdate_email() throws Exception {
        // 임의의 테스트용 객체
        String testId = "user1";
        UserDto testDto = userDao.select(testId);

        // 변경할 이메일
        String email = "updatedEmail@happydiary.com";
        // 이메일 수정 전
        assertNotEquals(email, userDao.select(testId).getEmail());
        // 이메일 수정
        testDto.setEmail(email);
        assertEquals(1, userDao.update(testDto)); // 수정 성공여부 확인
        // 이메일 수정 후
        assertEquals(email, userDao.select(testId).getEmail());
    }

    @Test
    @DisplayName("타입 길이 오류로 인한 이메일 수정 실패")
    void failToUpdateEmail_TypeLengthError() throws Exception {
        // 잘못된 타입길이로 설정한 항목이 포함된 테스트용 객체 testDto
        // email 컬럼의 타입 길이는 varchar(50)으로 설정되어 있으며, 그 이상의 데이터 저장 시도 시 관련 예외 발생하는지 확인
        String testId = "user1";
        String email = "wrong_type_length_email_123456789123456789123456789123456789123456789@happydiary.com";
        UserDto testDto = userDao.select(testId);
        testDto.setEmail(email);

        assertThrows(DataIntegrityViolationException.class, () -> userDao.update(testDto));
    }

    @Test
    @DisplayName("주소 수정 성공")
    void successOfUpdate_address() throws Exception {
        // 임의의 테스트용 객체
        String testId = "user1";
        UserDto testDto = userDao.select(testId);

        // 변경할 주소
        String address = "updated_Address";
        // 주소 수정 전
        assertNotEquals(address, userDao.select(testId).getAddress());
        // 주소 수정
        testDto.setAddress(address);
        assertEquals(1, userDao.update(testDto)); // 수정 성공여부 확인
        // 주소 수정 후
        assertEquals(address, userDao.select(testId).getAddress());
    }

    @Test
    @DisplayName("타입 길이 오류로 인한 주소 수정 실패")
    void failToUpdateAddress_TypeLengthError() throws Exception {
        // 잘못된 타입길이로 설정한 항목이 포함된 테스트용 객체 testDto
        // address 컬럼의 타입 길이는 varchar(50)으로 설정되어 있으며, 그 이상의 데이터 저장 시도 시 관련 예외 발생하는지 확인
        String testId = "user1";
        String address = "wrong_type_length_Address_123456789123456789123456789123456789123456789123456789";

        assertTrue(address.length() > 50);

        UserDto testDto = userDao.select(testId);
        testDto.setAddress(address);

        assertThrows(DataIntegrityViolationException.class, () -> userDao.update(testDto));
    }

    // delete 메서드 테스트
    @Test
    @DisplayName("데이터 삭제 성공")
    void successOfDelete() throws Exception {
        // 특정 데이터 존재여부 확인
        String testId = "user1";

        // 데이터 삭제 전
        assertNotNull(userDao.select(testId));
        // 데이터 삭제
        assertEquals(1, userDao.delete(testId));
        // 데이터 삭제 후
        assertNull(userDao.select(testId));
    }

    @Test
    @DisplayName("데이터 삭제 잘못 시도한 경우 삭제 실패")
    void failToDelete_wrongTry() throws Exception {
        // 존재하지 않는 데이터 삭제 시도 시
        String wrongId = "wrongId";
        assertNull(userDao.select(wrongId));
        // 데이터 삭제 실패
        assertEquals(0, userDao.delete(wrongId));
    }

    @Test
    @DisplayName("존재하지 않는 데이터 삭제 시도한 경우 삭제 실패")
    void failToDelete_nonexistData() throws Exception {
        String testId = "user1";
        assertNotNull(userDao.select(testId));

        assertEquals(1, userDao.delete(testId));
        assertNull(userDao.select(testId));

        // 존재하지 않는 데이터 삭제 시도 시
        // 데이터 삭제 실패
        assertEquals(0, userDao.delete(testId));
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 데이터 삭제 실패")
    void failToDBConnection_delete() throws Exception {
        String testId = "user1";

        // delete() 호출 시 DB 접근 예외 발생시킴
        when(mockDao.delete(testId)).thenThrow(new DataAccessException("Database connection error!"){});
        // 설정된 예외 발생여부 확인
        assertThrows(DataAccessException.class, () -> mockDao.delete(testId));
    }

    // deleteAll 메서드 테스트
    @Test
    @DisplayName("전체 데이터 삭제 성공")
    void successOfDeleteAll() throws Exception {
        // 현재 미리 셋팅된 데이터 존재함을 확인
        assertNotNull(userDao.selectAll());
        assertFalse(userDao.selectAll().isEmpty());

        // 전체 데이터 삭제
        // 셋팅해뒀던 40개의 row 삭제
        assertEquals(20, userDao.deleteAll());
        // 전체 데이터 삭제 후
        // 전체 데이터 조회 시 빈배열([]) 반환
        assertTrue(userDao.selectAll().isEmpty());
    }

    @Test
    @DisplayName("전체 데이터 삭제 실패")
    void failToDeleteAll() throws Exception {
        // 기존에 셋팅된 데이터 40개 전체 삭제
        assertEquals(20, userDao.deleteAll());
        // 데이터 모두 삭제된 상태임을 확인
        assertTrue(userDao.selectAll().isEmpty());
        // 데이터 없는 상태에서 전체 데이터 삭제 시도 시, 삭제되는 데이터 없음을 확인
        assertEquals(0, userDao.deleteAll());
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 전체 데이터 삭제 실패")
    void failToDBConnection_deleteAll() throws Exception {
        // deleteAll() 호출 시 DB 접근 예외 발생시킴
        when(mockDao.deleteAll()).thenThrow(new DataAccessException("Database connection error!"){});
        // 설정된 예외 발생여부 확인
        assertThrows(DataAccessException.class, () -> mockDao.deleteAll());
    }

    @Test
    @DisplayName("이메일로 직원 조회 성공")
    void successToSelectByEmail() throws Exception {
        // 기존에 셋팅된 데이터 활용하여 변수 ID, Email 선언 및 초기화
        String inputEmail = "user1@happydiary.com";
        String userId = "user1";
        UserDto result = userDao.selectByEmail(inputEmail);
        // UserDto 조회결과 확인
        assertNotNull(result);
        // Email 로 조회한 직원 정보에서 ID가 예상했던 결과와 일치하는지 확인
        assertEquals(userId, result.getId());
    }

    @Test
    @DisplayName("이메일로 직원 조회 실패")
    void failToSelectByEmail() throws Exception {
        String wrongEmail = "wrong@example.com";
        UserDto result = userDao.selectByEmail(wrongEmail);
        assertNull(result);
    }

    @Test
    @DisplayName("DB 연결 실패로 인한 이메일로 직원 조회 실패")
    void failToDBConnection_selectByEmail() throws Exception {
        // selectByEmail() 호출 시 DB 접근 예외 발생시킴
        String inputEmail = "test1@happydiary.com";
        when(mockDao.selectByEmail(inputEmail)).thenThrow(new DataAccessException("Database connection error!"){});
        // 설정된 예외 발생여부 확인
        assertThrows(DataAccessException.class, () -> mockDao.selectByEmail(inputEmail));
    }
}