package com.example.employee_management.controllers.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.services.EmployeeService;


@Controller
@RequestMapping("/admin/employees")
public class AdminEmployeeController {
    private final EmployeeService employeeService;

    public AdminEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String index(Model model) {
        List<EmployeeDTO> employees = employeeService.getAll();

        model.addAttribute("employees", employees);

        return "pages/employee/index";
    }

}
