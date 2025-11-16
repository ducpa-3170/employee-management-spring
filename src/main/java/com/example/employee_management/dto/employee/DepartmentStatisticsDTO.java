package com.example.employee_management.dto.employee;

public class DepartmentStatisticsDTO {
    private String departmentName;
    private Long employeeCount;

    // Constructors
    public DepartmentStatisticsDTO() {
    }

    public DepartmentStatisticsDTO(String departmentName, Long employeeCount) {
        this.departmentName = departmentName;
        this.employeeCount = employeeCount;
    }

    // Getters and Setters
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Long employeeCount) {
        this.employeeCount = employeeCount;
    }
}
