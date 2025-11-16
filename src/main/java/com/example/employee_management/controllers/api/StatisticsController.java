package com.example.employee_management.controllers.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_management.dto.employee.DepartmentStatisticsDTO;
import com.example.employee_management.services.EmployeeService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final EmployeeService employeeService;

    public StatisticsController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees-by-department")
    public ResponseEntity<List<DepartmentStatisticsDTO>> getEmployeesByDepartment() {
        List<DepartmentStatisticsDTO> statistics = employeeService.getDepartmentStatistics();
        return ResponseEntity.ok(statistics);
    }
}
