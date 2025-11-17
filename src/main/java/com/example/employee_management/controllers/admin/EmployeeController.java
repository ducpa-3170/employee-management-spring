package com.example.employee_management.controllers.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_management.aspect.Loggable;
import com.example.employee_management.dto.DepartmentDTO;
import com.example.employee_management.dto.employee.EmployeeCreateDTO;
import com.example.employee_management.dto.employee.EmployeeDTO;
import com.example.employee_management.dto.employee.EmployeeUpdateDTO;
import com.example.employee_management.services.DepartmentService;
import com.example.employee_management.services.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @Loggable
    @GetMapping
    public String index(Model model,
            @RequestParam(required = false) String keyword) {

        List<EmployeeDTO> employees;

        if (keyword != null && !keyword.trim().isEmpty()) {
            employees = employeeService.search(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            employees = employeeService.getAll();
        }

        model.addAttribute("employees", employees);

        return "pages/employee/index";
    }

    @GetMapping("/create")
    public String create(Model model) {

        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new EmployeeCreateDTO());
        }

        model.addAttribute("departments", departments);

        return "pages/employee/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("employee") EmployeeCreateDTO employeeCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("employee", employeeCreateDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employee",
                    bindingResult);
            return "redirect:/admin/employees/create";
        }

        try {
            employeeService.create(employeeCreateDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Employee created successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating employee: " + e.getMessage());
            return "redirect:/admin/employees/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        EmployeeDTO employeeDTO = employeeService.getById(id);
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        model.addAttribute("employee", employeeDTO);
        model.addAttribute("departments", departments);

        return "pages/employee/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("employee") EmployeeUpdateDTO employeeUpdateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("employee", employeeUpdateDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employee",
                    bindingResult);
            return "redirect:/admin/employees/edit/" + id;
        }

        try {
            employeeService.update(id, employeeUpdateDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating employee: " + e.getMessage());
            return "redirect:/admin/employees/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            employeeService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee: " + e.getMessage());
            return "redirect:/admin/employees";
        }
    }
}
