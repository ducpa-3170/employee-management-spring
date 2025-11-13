package com.example.employee_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_management.models.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
