package com.happydiary.controller;

import com.happydiary.common.validation.ValidationGroups;
import com.happydiary.dto.UserDto;
import com.happydiary.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserServiceImpl userService;

    @GetMapping("/form")
    public String moveToLoginPage() {
        return "login";
    }

    @GetMapping("/in/test")
    public String testPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("loginStatus", "Y");
        session.setAttribute("userId", "test");
        session.setAttribute("userName", userService.getUserInfo("test").getName());
        return "redirect:/board/home";
    }

    @PostMapping("in")
    public String login(@Validated(ValidationGroups.LoginCheckGroup.class) UserDto userDto, BindingResult bindingResult, boolean rememberMe, HttpServletResponse response, HttpServletRequest request, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errorMsg = bindingResult.getAllErrors();
            ra.addFlashAttribute("errorMsg", errorMsg.get(0).getDefaultMessage());
            return "redirect:/login/form";
        }

        String id = userDto.getId();

        // ID 기억하기 체크박스
        if (rememberMe) {
            Cookie cookie = new Cookie("savedId", id);
            response.addCookie(cookie); // ID 저장
        } else {
            Cookie cookie = new Cookie("savedId", "");
            cookie.setMaxAge(0); // ID 삭제
            response.addCookie(cookie);
        }

        if (!userService.checkExistOfId(id)) {
            ra.addFlashAttribute("nonExistIdMsg", "존재하지 않는 아이디입니다.");
            return "redirect:/login/form";
        } else if (!userService.validateUserLogin(userDto)) {
            ra.addFlashAttribute("notMatchMsg", "ID와 Password가 일치하지 않습니다.");
            return "redirect:/login/form";
        }

        String name = userService.getUserInfo(id).getName();

        // 쿠키는 클라이언트 측에서 조작할 수 있는 위험성이 있음(보안에 취약)
        // 세션에 현재의 로그인 상태 및 사용자 아이디, 사용자 이름을 저장하여 관리
        HttpSession session = request.getSession();
        session.setAttribute("loginStatus", "Y");
        session.setAttribute("userId", id);
        session.setAttribute("userName", name);

        return "redirect:/board/home";
    }

    @GetMapping("/out")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login/form";
    }
}
