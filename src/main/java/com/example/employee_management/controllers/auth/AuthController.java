package com.example.employee_management.controllers.auth;

import com.example.employee_management.dto.auth.RegisterDTO;
import com.example.employee_management.models.User;
import com.example.employee_management.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "pages/auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "pages/auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Validate password match
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.registerDTO", "Passwords do not match");
        }

        // Check if username already exists
        if (userService.existsByUsername(registerDTO.getUsername())) {
            result.rejectValue("username", "error.registerDTO", "Username already exists");
        }

        // Check if email already exists
        if (userService.existsByEmail(registerDTO.getEmail())) {
            result.rejectValue("email", "error.registerDTO", "Email already exists");
        }

        if (result.hasErrors()) {
            return "pages/auth/register";
        }

        try {
            // Create new user
            User user = new User();
            user.setUsername(registerDTO.getUsername());
            user.setEmail(registerDTO.getEmail());
            user.setPassword(registerDTO.getPassword());
            user.setRole(registerDTO.getRole());
            user.setEnabled(true);

            userService.save(user);

            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "pages/auth/register";
        }
    }
}
