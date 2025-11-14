package com.example.employee_management.dto.employee;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmployeeUpdateDTO {
    @NotNull
    private Long id;

    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    private String name;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email is invalid.")
    private String email;

    private Long departmentId;

    public EmployeeUpdateDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
