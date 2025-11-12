package com.example.employee_management.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.exception.NotFoundException;
import com.example.employee_management.models.Department;
import com.example.employee_management.models.Employee;
import com.example.employee_management.repositories.DepartmentRepository;
import com.example.employee_management.repositories.EmployeeRepository;

@Service
public class EmployeeService extends AbstractService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(ModelMapper modelMapper, EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {
        super(modelMapper);
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public EmployeeDTO convertToDto(Employee employee) {
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);

        if (employee.getDepartment() != null) {
            employeeDTO.setDepartmentId(employee.getDepartment().getId());
        }

        return employeeDTO;
    }

    public List<EmployeeDTO> getAll() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(this::convertToDto)
                .toList();
    }

    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO, Employee.class);

        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new NotFoundException(
                            "Department not found with id: " + employeeDTO.getDepartmentId()));
            employee.setDepartment(department);
        }

        Employee savedEmployee = employeeRepository.save(employee);

        return convertToDto(savedEmployee);
    }

    public EmployeeDTO update(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (employeeDTO.getName() != null) {
            existingEmployee.setName(employeeDTO.getName());
        }

        if (employeeDTO.getEmail() != null) {
            existingEmployee.setEmail(employeeDTO.getEmail());
        }

        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new NotFoundException(
                            "Department not found with id: " + employeeDTO.getDepartmentId()));
            existingEmployee.setDepartment(department);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return convertToDto(updatedEmployee);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeDTO> searchByName(String name) {
        List<Employee> employees = employeeRepository.findByNameContainingIgnoreCase(name);

        if (employees.isEmpty()) {
            throw new NotFoundException("No employee found with name containing: " + name);
        }

        return employees.stream()
                .map(this::convertToDto)
                .toList();
    }
}
