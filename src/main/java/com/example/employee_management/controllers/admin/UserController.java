package com.example.employee_management.controllers.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_management.dto.auth.UserDTO;
import com.example.employee_management.dto.user.UserCreateDTO;
import com.example.employee_management.models.Role;
import com.example.employee_management.services.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getMethodName(Model model,
            @RequestParam(required = false) String keyword) {
        List<UserDTO> users = userService.getAll();

        model.addAttribute("users", users);

        return "pages/user/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        model.addAttribute("roles", Role.values());
        return "pages/user/create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") UserCreateDTO userCreateDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (!userService.isPasswordMatch(userCreateDTO.getPassword(), userCreateDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Confirm password does not match.");
        }

        if (userService.existsByUsername(userCreateDTO.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists.");
        }

        if (userService.existsByEmail(userCreateDTO.getEmail())) {
            result.rejectValue("email", "error.user", "Email is already in use.");
        }

        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "pages/user/create";
        }

        try {
            userService.createUser(userCreateDTO);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while creating user: " + e.getMessage());
            model.addAttribute("roles", Role.values());
            return "pages/user/create";
        }
    }

}
