package com.happydiary.controller;

import com.happydiary.dto.UserDto;
import com.happydiary.service.UserServiceImpl;
import com.happydiary.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/userInfo", produces = "application/text;charset=UTF-8")
public class UserInfoController {
    private final UserServiceImpl userService;
    private final MailService mailService;

    @GetMapping("/modify")
    public String modify(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("userId");
        model.addAttribute("userDto", userService.getUserInfo(id));
        return "modifyUserInfo";
    }

    // 비밀번호 변경
    // 1. 현재 비밀번호 일치 여부 확인
    // 1.1. 현재 비밀번호 일치하는 경우
    // 1.1.1. 변경된 비밀번호 고객 정보에 저장
    // 1.2. 현재 비밀번호 일치하지 않은 경우 or 기존 비밀번호와 동일한 비밀번호로 변경 시도한 경우
    // 1.2.1. 기존 화면에서 오류메세지 출력
    @PostMapping("modifyPwd")
    public ResponseEntity<String> modifyPwd(String pwd, String newPwd, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String id = (String) session.getAttribute("userId");
            UserDto userDto = userService.getUserInfo(id);
            String dbPwd = userDto.getPwd();

            if (pwd.equals(newPwd)) {
                return new ResponseEntity<>("기존 비밀번호와 동일합니다.", HttpStatus.BAD_REQUEST);
            } else if (userService.authenticatePwd(pwd, dbPwd)) {
                userService.modifyPassword(id, newPwd);
                return new ResponseEntity<>("비밀번호 변경 성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("잘못된 비밀번호입니다. 다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return new ResponseEntity<>("[ERROR] 비밀번호 변경 시도 과정에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 이메일 변경
    // 1. 새로운 이메일 주소 중복 여부 확인
    // 1.1. 중복된 이메일인 경우
    // 1.1.1. 오류메세지 출력
    // 1.2. 중복되지 않은 이메일인 경우
    // 1.2.1. 새로운 이메일 주소로 인증번호 전송
    @PostMapping("/verification")
    public ResponseEntity<String> modifyEmail(String newEmail, HttpServletRequest request) {
        // 세션에 저장된 인증번호와 새로 생성한 인증번호 확인
        HttpSession session = request.getSession();
        String savedMailKey = (String) session.getAttribute("mailKey");
        String newMailKey = mailService.makeRandomMailKey();
        // 최종적으로 정해진 인증번호를 메일로 전송
        String mailKey = userService.makeVerificationCode(savedMailKey, newMailKey);

        try {
            if (userService.findIdByEmail(newEmail).isEmpty()) {
                userService.sendVerificationEmail(newEmail, mailKey); // 메일 전송
                session.setAttribute("mailKey", mailKey); // 세션에 인증번호 저장
                session.setMaxInactiveInterval(60); // 세션 만료 시간 설정(1분)
                return new ResponseEntity<>("인증번호 전송 성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("이미 사용중인 이메일입니다. 다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return new ResponseEntity<>("[ERROR] 이메일 변경 시도 과정에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/modifyEmail")
    public ResponseEntity<String> modifyEmail(String newEmail, String verificationCode, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("userId");
        String savedMailKey = (String) session.getAttribute("mailKey");

        try {
            if (savedMailKey.equals(verificationCode)) {
                UserDto userDto = userService.getUserInfo(id);
                userDto.setEmail(newEmail);
                userService.modifyUserInfo(userDto);
                return new ResponseEntity<>("이메일 변경 성공.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("잘못된 인증번호입니다. 다시 입력해주십시오.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return new ResponseEntity<>("[ERROR] 메일인증 시도 과정에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/modifyPhoneNum")
    public ResponseEntity<String> modifyPhoneNum(String phone_num, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("userId");
        UserDto userDto = userService.getUserInfo(id);
        String dbPhoneNum = userDto.getPhone_num();

        try {
            if (!dbPhoneNum.equals(phone_num)) {
                userDto.setPhone_num(phone_num);
                userService.modifyUserInfo(userDto);
                return new ResponseEntity<>("휴대폰번호 변경 성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("기존의 휴대폰번호와 동일한 번호입니다. 다시 입력해주십시오.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return new ResponseEntity<>("[ERROR] 휴대폰번호 변경 시도 과정에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
