package com.example.employee_management.dto.employee;

public class TotalEmployeeDTO {
    private Long totalEmployees;

    public TotalEmployeeDTO() {
    }

    public TotalEmployeeDTO(Long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public Long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(Long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }
}
