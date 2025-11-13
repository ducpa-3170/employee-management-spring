package com.example.employee_management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.employee_management.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByNameContainingIgnoreCase(String name);

    @Query("SELECT e FROM Employee e LEFT JOIN e.department d " +
            "WHERE :keyword IS NULL " +
            "OR LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> searchByKeyword(@Param("keyword") String keyword);
}
