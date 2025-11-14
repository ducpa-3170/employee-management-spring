package com.example.employee_management.controllers.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.example.employee_management.dto.DepartmentDTO;
import com.example.employee_management.dto.employee.EmployeeCreateDTO;
import com.example.employee_management.dto.employee.EmployeeDTO;
import com.example.employee_management.dto.employee.EmployeeUpdateDTO;
import com.example.employee_management.services.DepartmentService;
import com.example.employee_management.services.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/employees")
public class AdminEmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(AdminEmployeeController.class);

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public AdminEmployeeController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String index(Model model,
            @RequestParam(required = false) String keyword) {
        logger.info("Accessing employee list page. Keyword: {}", keyword);

        List<EmployeeDTO> employees;

        if (keyword != null && !keyword.trim().isEmpty()) {
            logger.debug("Searching employees with keyword: {}", keyword);
            employees = employeeService.search(keyword);
            model.addAttribute("keyword", keyword);
            logger.info("Found {} employees matching keyword: {}", employees.size(), keyword);
        } else {
            logger.debug("Retrieving all employees");
            employees = employeeService.getAll();
            logger.info("Retrieved {} employees", employees.size());
        }

        model.addAttribute("employees", employees);

        return "pages/employee/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        logger.info("Accessing employee create page");

        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        logger.debug("Retrieved {} departments for employee creation", departments.size());

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

        logger.info("Attempting to create new employee: {}", employeeCreateDTO.getName());

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred while creating employee: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("employee", employeeCreateDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employee",
                    bindingResult);
            return "redirect:/admin/employees/create";
        }

        try {
            employeeService.create(employeeCreateDTO);
            logger.info("Successfully created employee: {}", employeeCreateDTO.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Employee created successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            logger.error("Error creating employee: {}", employeeCreateDTO.getName(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating employee: " + e.getMessage());
            return "redirect:/admin/employees/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        logger.info("Accessing employee edit page for ID: {}", id);

        EmployeeDTO employeeDTO = employeeService.getById(id);
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        logger.debug("Retrieved employee: {} and {} departments", employeeDTO.getName(), departments.size());

        model.addAttribute("employee", employeeDTO);
        model.addAttribute("departments", departments);

        return "pages/employee/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("employee") EmployeeUpdateDTO employeeUpdateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        logger.info("Attempting to update employee with ID: {}", id);

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred while updating employee ID {}: {}", id,
                    bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("employee", employeeUpdateDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employee",
                    bindingResult);
            return "redirect:/admin/employees/edit/" + id;
        }

        try {
            employeeService.update(id, employeeUpdateDTO);
            logger.info("Successfully updated employee ID: {}", id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            logger.error("Error updating employee ID: {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating employee: " + e.getMessage());
            return "redirect:/admin/employees/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to delete employee with ID: {}", id);

        try {
            employeeService.delete(id);
            logger.info("Successfully deleted employee ID: {}", id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            logger.error("Error deleting employee ID: {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee: " + e.getMessage());
            return "redirect:/admin/employees";
        }
    }
}
