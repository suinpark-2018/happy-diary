package com.happydiary.controller;

import com.happydiary.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/delete")
@RequiredArgsConstructor
public class AccountDeletionController {
    private final UserServiceImpl userService;

    @PostMapping("/account")
    public String accountDeletion(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("userId");

        if (!id.isEmpty() || !id.equals(" ")) {
            userService.deleteUserAccount(id);
            session.invalidate();
        }

        return "redirect:/";
    }
}