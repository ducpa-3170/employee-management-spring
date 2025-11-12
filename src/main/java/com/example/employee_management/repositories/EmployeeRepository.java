package com.example.employee_management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_management.models.Employee;

public interface EmployeeRepository  extends JpaRepository<Employee, Long> {
    Employee findByNameContainingIgnoreCase(String name);
}
