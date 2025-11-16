package com.example.employee_management.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == 404) {
                model.addAttribute("message", "Page Not Found");
                model.addAttribute("details", "The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.");
                return "pages/404";
            } else if (statusCode == 403) {
                model.addAttribute("message", "Access Denied");
                model.addAttribute("details", "You don't have permission to access this resource.");
                return "pages/403";
            } else if (statusCode == 500) {
                model.addAttribute("message", "Internal Server Error");
                model.addAttribute("details", "Something went wrong on our end. Please try again later.");
                return "pages/500";
            }
        }

        model.addAttribute("message", "An error occurred");
        model.addAttribute("details", "Please try again or contact support if the problem persists.");
        return "pages/500";
    }

    @RequestMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("message", "Access Denied");
        model.addAttribute("details", "You don't have permission to access this resource.");
        return "pages/403";
    }
}
