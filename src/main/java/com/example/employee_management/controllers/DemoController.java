package com.example.employee_management.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class DemoController {
    @GetMapping("/hello")
    public String sayHello(Model model) {
        String greeting = "Hello, World!";

        model.addAttribute("greeting", greeting);

        return "pages/employee/index";
    }
}
