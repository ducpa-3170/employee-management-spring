package com.example.employee_management.controllers.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_management.dto.DepartmentDTO;
import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.services.DepartmentService;
import com.example.employee_management.services.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/employees")
public class AdminEmployeeController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public AdminEmployeeController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String index(Model model) {
        List<EmployeeDTO> employees = employeeService.getAll();

        model.addAttribute("employees", employees);

        return "pages/employee/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new EmployeeDTO());
        }

        model.addAttribute("departments", departments);

        return "pages/employee/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        System.out.println(employeeDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("employee", employeeDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employee", bindingResult);
            return "redirect:/admin/employees/create";
        }

        try {
            employeeService.create(employeeDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Employee created successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating employee: " + e.getMessage());
            return "redirect:/admin/employees/create";
        }
    }

}
