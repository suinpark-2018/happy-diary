package com.happydiary.service;

import com.happydiary.dao.UserDaoImpl;
import com.happydiary.dto.UserDto;
import com.happydiary.service.mail.MailService;
import com.happydiary.service.mail.MockMailSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
class UserServiceImplTest {
    @Autowired UserDaoImpl userDao;
    @Autowired UserServiceImpl userService;
    @Autowired JavaMailSender mailSender;
    @Autowired MailService mailService;

    // Mock 객체 초기화
    @BeforeEach
    void setUpMock() {
        MockitoAnnotations.openMocks(this);
    }

    // 더미데이터 추가 (비밀번호 암호화 적용)
    @BeforeEach
    void setUpDB_autheticatedPwd() throws Exception {
        for (int i = 1; i <= 20; i++) {
            UserDto testDto = new UserDto("user" + i, "Password1!", "name", "user" + i + "@spring.co.kr", "010-1234-5678", "19950102", "M", "서울시 강남구 영동대로");
            userService.saveUserJoinInfo(testDto);
        }
        for (int i = 21; i <= 40; i++) {
            UserDto testDto = new UserDto("user" + i, "Password1!", "name", "user" + i + "@spring.co.kr", "010-1234-5678", "19930401", "F", "서울시 강남구 영동대로");
            userService.saveUserJoinInfo(testDto);
        }
    }

    // 더미데이터 삭제
    @AfterEach
    void cleanDB() throws Exception {
        userDao.deleteAll();
    }


    @Test
    @DisplayName("아이디 조회 성공")
    void successToCheckExistOfId() {
        String existId = "user1"; // 더미데이터로 넣은 ID
        assertTrue(userService.checkExistOfId(existId));
    }

    @Test
    @DisplayName("아이디 조회 실패")
    void failToCheckExistOfId() {
        String nonExistId = "nonExistId";
        assertFalse(userService.checkExistOfId(nonExistId));
    }

    @Test
    @DisplayName("회원가입 시 입력데이터 저장 성공")
    void successToSaveUserJoinInfo() {
        String testId = "test2024";
        UserDto testDto = new UserDto(testId, "Password1!", "name", "test@spring.co.kr", "010-1234-5678", "19950102", "M", "서울시 강남구 영동대로");

        // 데이터 저장 성공 시 true 반환
        assertTrue(userService.saveUserJoinInfo(testDto));
        // 저장 시도한 데이터(ID)가 조회되면 true 반환
        assertTrue(userService.checkExistOfId(testId));
    }

    @Test
    @DisplayName("회원가입 시 중복된 ID 입력 시 데이터 저장 실패")
    void failToSaveUserJoinInfo() {
        String duplicatedId = "user1"; // 이미 DB에 저장된 ID
        assertTrue(userService.checkExistOfId(duplicatedId));

        UserDto testDto = new UserDto(duplicatedId, "Password1!", "name", "test@spring.co.kr", "010-1234-5678", "19950102", "M", "서울시 강남구 영동대로");
        assertFalse(userService.saveUserJoinInfo(testDto));
    }

    @Test
    @DisplayName("중복된 아이디 존재하는 경우 확인 성공")
    void successToCheckDuplicatedId() {
        String duplicatedId = "user1";
        assertTrue(userService.checkExistOfId(duplicatedId));
    }

    @Test
    @DisplayName("중복된 아이디 존재하지 않는 경우 확인 성공")
    void successToCheckNonDuplicatedId() {
        String nonDuplicatedId = "newID";
        assertFalse(userService.checkExistOfId(nonDuplicatedId));
    }

    @Test
    @DisplayName("로그인 성공")
    void successToValidateUserLogin() {
        // DB에 저장된 ID, PWD 값과 동일한 ID, PWD 값으로 초기화
        String inputId = "user1";
        String inputPwd = "Password1!";
        assertTrue(userService.checkExistOfId(inputId));

        UserDto testDto = new UserDto();
        testDto.setId(inputId);
        testDto.setPwd(inputPwd);

        assertTrue(userService.validateUserLogin(testDto));
    }

    @Test
    @DisplayName("존재하지 않는 아이디 입력 시 로그인 실패")
    void failToValidateUserLogin_inputNonExistId() {
        String inputId = "wrongId";
        String inputPwd = "password1!";
        assertFalse(userService.checkExistOfId(inputId));

        UserDto testDto = new UserDto();
        testDto.setId(inputId);
        testDto.setPwd(inputPwd);

        assertFalse(userService.validateUserLogin(testDto));
    }

    @Test
    @DisplayName("비밀번호 암호화 성공")
    void successToEncodePwd() {
        UserDto testDto = new UserDto("TestId2024", "Password1!", "name", "test@spring.co.kr", "010-1234-5678", "19950102", "M", "서울시 강남구 영동대로");
        assertTrue(userService.saveUserJoinInfo(testDto));
    }

    @Test
    @DisplayName("기존 비밀번호와 암호화된 비밀번호 비교 성공")
    void successToAuthenticatePwd() {
        UserDto testDto = new UserDto("TestId2024", "Password1!", "name", "test@spring.co.kr", "010-1234-5678", "19950102", "M", "서울시 강남구 영동대로");
        assertTrue(userService.saveUserJoinInfo(testDto));

        String inputId = "TestId2024";
        String inputPwd = "Password1!";

        UserDto testDto2 = new UserDto();
        testDto2.setId(inputId);
        testDto2.setPwd(inputPwd);

        assertTrue(userService.validateUserLogin(testDto2));
    }

    @Test
    @DisplayName("잘못된 비밀번호 입력 시 로그인 실패")
    void failToValidateUserLogin_inputWrongPwd() {
        String inputId = "user1";
        String inputPwd = "wrongPwd1!";
        assertTrue(userService.checkExistOfId(inputId));

        UserDto testDto = new UserDto();
        testDto.setId(inputId);
        testDto.setPwd(inputPwd);

        assertFalse(userService.validateUserLogin(testDto));
    }

    @Test
    @DisplayName("특정 직원 정보 조회 성공")
    void successToGetUserInfo() {
        // 더미데이터로 미리 저장해둔 ID 활용
        String id = "user1";
        assertNotNull(userService.getUserInfo(id));

        // 새로운 정보 추가로 저장하여 확인
        String newId = "TestID";
        String newName = "TestName";
        String newEmail = "Test@spring.co.kr";
        assertNull(userService.getUserInfo(newId));

        UserDto testDto = new UserDto(newId, "Password1!", newName, newEmail, "010-1234-5678", "19950102", "M", "서울시 강남구 영동대로");
        assertTrue(userService.saveUserJoinInfo(testDto));

        assertNotNull(userService.getUserInfo(newId));
        UserDto result = userService.getUserInfo(newId);

        assertEquals(newId, result.getId());
        assertEquals(newName, result.getName());
        assertEquals(newEmail, result.getEmail());
    }

    @Test
    @DisplayName("특정 직원 정보 조회 실패")
    void failToGetUserInfo() {
        // 잘못된 ID로 조회 시 조회 실패
        String id = "wrongId";
        assertNull(userService.getUserInfo(id));
    }

    @Test
    @DisplayName("메일 전송 오브젝트 주입 성공")
    void successToDIOfMailSender() {
        assertNotNull(mailService);
    }

    @Test
    @DisplayName("메일 전송 성공")
    void successToSendMail() {
        MockMailSender mockMailSender = new MockMailSender();
        mailService = new MailService(mockMailSender);

        // 테스트용 UserDto 생성
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");

        mailService.sendTestEmail(userDto);
    }

    @Test
    @DisplayName("아이디 찾기 성공")
    void successToFindId() {
        String inputEmail = "user1@spring.co.kr";
        String expectedId = "user1";

        assertNotNull(userService.findIdByEmail(inputEmail));
        assertEquals(expectedId, userService.findIdByEmail(inputEmail));
    }

    @Test
    @DisplayName("존재하지 않는 이메일 정보로 조회 시 아이디 찾기 실패")
    void failToFindId() {
        String inputEmail = "wrongEmail@spring.co.kr";
        String expectedId = "user1";

        assertTrue(userService.findIdByEmail(inputEmail).isBlank());
        assertNotEquals(expectedId, userService.findIdByEmail(inputEmail));
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void successToModifyPwd() {
        String inputId = "user1";
        String inputPwd = "NewPassword1@";
        // 비밀번호 변경 전
        UserDto testDto = new UserDto();
        testDto.setId(inputId);
        testDto.setPwd(inputPwd);
        assertFalse(userService.validateUserLogin(testDto));
        // 비밀번호 변경
        assertTrue(userService.modifyPassword(inputId, inputPwd));
        // 비밀번호 변경 후
        assertTrue(userService.validateUserLogin(testDto));
    }

    @Test
    @DisplayName("잘못된 아이디로 인한 비밀번호 변경 실패")
    void failToModifyPwd() {
        String inputId = "wrongId";
        String inputPwd = "NewPassword1@";
        // 비밀번호 변경 전
        UserDto testDto = new UserDto();
        testDto.setId(inputId);
        testDto.setPwd(inputPwd);
        assertFalse(userService.validateUserLogin(testDto));
        // 비밀번호 변경
        assertFalse(userService.modifyPassword(inputId, inputPwd));
    }
}