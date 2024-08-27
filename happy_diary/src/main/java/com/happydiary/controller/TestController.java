package com.happydiary.controller;

import com.happydiary.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @Autowired TestServiceImpl testService;

    @RequestMapping("/")
    public String now(Model model) {
        String testResult = testService.now();
        if (!testResult.isEmpty() || !testResult.isBlank()) {
            model.addAttribute("success", testResult);
        } else {
            model.addAttribute("fail", "fail to connect");
        }
        return "index";
    }
}
