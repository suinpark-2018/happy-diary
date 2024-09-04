package com.happydiary.controller;

import com.happydiary.common.validation.ValidationGroups;
import com.happydiary.dto.UserDto;
import com.happydiary.service.UserServiceImpl;
import com.happydiary.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/find", produces = "application/json;charset=UTF-8")
public class FindIdPwdController {

    private final UserServiceImpl userService;
    private final MailService mailService;

    @GetMapping("/idAndPwd")
    public String findIdAndPassword() {
        return "findIdPwd";
    }

    @PostMapping("/id/verify")
    @ResponseBody
    public ResponseEntity<String> findIdVerify(@RequestBody @Validated(ValidationGroups.EmailCheckGroup.class) UserDto userDto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorMsg = bindingResult.getAllErrors();
            return new ResponseEntity<>(errorMsg.get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        // 사용자가 입력한 이메일
        String email = userDto.getEmail();

        // 세션에 저장된 인증번호와 새로 생성한 인증번호 확인
        HttpSession session = request.getSession();
        String savedMailKey = (String) session.getAttribute("mailKey");
        String newMailKey = mailService.makeRandomMailKey();
        // 최종적으로 정해진 인증번호를 메일로 전송
        String mailKey = userService.makeVerificationCode(savedMailKey, newMailKey);

        // 사용자가 입력한 이메일로 아이디 조회한 결과
        String selectedId = userService.findIdByEmail(email);

        try {
            if (!selectedId.isEmpty() || !selectedId.isBlank()) {
                if (userService.sendVerificationEmail(email, mailKey)) {
                    session.setAttribute("mailKey", mailKey); // 세션에 인증번호 저장
                    session.setMaxInactiveInterval(60); // 세션 만료 시간 설정(1분)
                    return new ResponseEntity<>("인증번호 전송에 성공했습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("인증번호 전송에 실패했습니다.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("존재하지 않는 이메일입니다.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return new ResponseEntity<>("인증번호 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 인증번호 일치여부 확인
    @PostMapping("/id/verification")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verificationCodeToFindId(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        // 세션에 저장해둔 인증번호(savedMailKey)
        HttpSession session = request.getSession();
        String savedMailKey = (String) session.getAttribute("mailKey");

        // 사용자가 입력한 인증번호(inputMailKey)
        String inputMailKey = requestBody.get("verificationCode");

        try {
            if (inputMailKey.equals(savedMailKey)) {
                // 사용자가 입력한 이메일로 아이디 조회
                String email = requestBody.get("email");
                String id = userService.findIdByEmail(email);

                // 결과를 JSON으로 반환
                Map<String, String> response = new HashMap<>();
                response.put("message", "인증 성공했습니다.");
                response.put("id", id); // 조회된 아이디 반환
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "인증번호가 일치하지 않습니다.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/pwd/verify")
    @ResponseBody
    public ResponseEntity<String> findPwdVerify(@RequestBody @Validated({ValidationGroups.EmailCheckGroup.class, ValidationGroups.IdCheckGroup.class}) UserDto userDto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            List<ObjectError> errorMsg = bindingResult.getAllErrors();
            return new ResponseEntity<>(errorMsg.get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        // 사용자가 입력한 아이디, 이메일
        String id = userDto.getId();
        String email = userDto.getEmail();

        // 세션에 저장된 인증번호와 새로 생성한 인증번호 확인
        HttpSession session = request.getSession();
        String savedMailKey = (String) session.getAttribute("mailKey");
        String newMailKey = mailService.makeRandomMailKey();
        // 최종적으로 정해진 인증번호를 메일로 전송
        String mailKey = userService.makeVerificationCode(savedMailKey, newMailKey);

        // 사용자가 입력한 이메일로 아이디 조회한 결과
        String selectedId = userService.findIdByEmail(email);

        // 사용자가 입력한 아이디 확인 시
        // 조회된 아이디가 존재하는 경우
        // 사용자가 입력한 이메일로 조회된 사용자의 아이디와 일치하는 경우
        // 인증번호 전송
        try {
            if ((!selectedId.isEmpty() || !selectedId.isBlank()) && id.equals(selectedId)) {
                if (userService.sendVerificationEmail(email, mailKey)) {
                    session.setAttribute("mailKey", mailKey); // 세션에 인증번호 저장
                    session.setMaxInactiveInterval(60); // 세션 만료 시간 설정(1분)
                    return new ResponseEntity<>("인증번호 전송에 성공했습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("인증번호 전송에 실패했습니다.", HttpStatus.BAD_REQUEST);
                }
            } else if (!id.equals(selectedId)) {
                return new ResponseEntity<>("아이디 또는 이메일을 다시 확인해주세요.", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("존재하지 않는 이메일입니다.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return new ResponseEntity<>("인증번호 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 인증번호 일치여부 확인
    @PostMapping("/pwd/verification")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verificationCodeToFindPwd(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60); // 세션만료시간 1분으로 설정

        // 세션에 저장해둔 인증번호(savedMailKey)
        String savedMailKey = (String) session.getAttribute("mailKey");

        // 사용자가 입력한 인증번호(inputMailKey)
        String inputMailKey = requestBody.get("verificationCode");
        String userId = requestBody.get("id");

        Map<String, String> response = new HashMap<>();
        try {
            if (inputMailKey.equals(savedMailKey)) {
                response.put("message", "인증 성공했습니다.");
                session.setAttribute("userIdToFindPwd", userId);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "인증번호가 일치하지 않습니다.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            response.put("message", "서버 오류가 발생했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pwd/modify")
    public String modifyPwd() {
        return "modifyPwd";
    }

    @PostMapping("/pwd/modify")
    public String modifyPwd2(@Validated(ValidationGroups.LoginCheckGroup.class) UserDto userDto, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes ra) {
        String errorMsg = "";
        if (bindingResult.hasErrors()) {
            List<ObjectError> errorMsgs = bindingResult.getAllErrors();
            errorMsg = errorMsgs.get(0).getDefaultMessage();
            model.addAttribute("errorMsg", errorMsgs);
            return "modifyPwd";
        }

        String id = userDto.getId();
        String pwd = userDto.getPwd();

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userIdToFindPwd");

        if (userService.modifyPassword(id, pwd)) {
            ra.addFlashAttribute("pwdUpdateState", "비밀번호 변경 완료");
            return "redirect:/login/form";
        } else if (userId == null) {
            ra.addFlashAttribute("pwdUpdateState", "비밀번호 변경 실패! 세션이 만료되어 메일 재인증이 필요합니다.");
            return "redirect:/find/idAndPwd";
        } else {
            ra.addFlashAttribute("pwdUpdateState", "비밀번호 변경 실패! 비밀번호 변경을 다시 시도해주세요.");
            return "redirect:/find/pwd/modify";
        }
    }
}