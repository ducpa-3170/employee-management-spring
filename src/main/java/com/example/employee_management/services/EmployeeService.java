package com.example.employee_management.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.employee_management.dto.employee.DepartmentStatisticsDTO;
import com.example.employee_management.dto.employee.EmployeeCreateDTO;
import com.example.employee_management.dto.employee.EmployeeDTO;
import com.example.employee_management.dto.employee.EmployeeUpdateDTO;
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
            employeeDTO.setDepartmentName(employee.getDepartment().getName());
        }

        return employeeDTO;
    }

    public List<EmployeeDTO> getAll() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<EmployeeDTO> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        String searchKeyword = keyword.trim();

        List<Employee> employees = employeeRepository.searchByKeyword(searchKeyword);

        return employees.stream()
                .map(this::convertToDto)
                .toList();
    }

    public EmployeeDTO create(EmployeeCreateDTO employeeCreateDTO) {
        Employee employee = convertToEntity(employeeCreateDTO, Employee.class);

        if (employeeCreateDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeCreateDTO.getDepartmentId())
                    .orElseThrow(() -> new NotFoundException(
                            "Department not found with id: " + employeeCreateDTO.getDepartmentId()));
            employee.setDepartment(department);
        }

        Employee savedEmployee = employeeRepository.save(employee);

        return convertToDto(savedEmployee);
    }

    public EmployeeDTO update(Long id, EmployeeUpdateDTO employeeUpdateDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (employeeUpdateDTO.getName() != null) {
            existingEmployee.setName(employeeUpdateDTO.getName());
        }

        if (employeeUpdateDTO.getEmail() != null) {
            existingEmployee.setEmail(employeeUpdateDTO.getEmail());
        }

        if (employeeUpdateDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeUpdateDTO.getDepartmentId())
                    .orElseThrow(() -> new NotFoundException(
                            "Department not found with id: " + employeeUpdateDTO.getDepartmentId()));
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

    public EmployeeDTO getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id: " + id));

        return convertToDto(employee);
    }

    public List<DepartmentStatisticsDTO> getDepartmentStatistics() {
        return employeeRepository.countEmployeesByDepartment();
    }

    public Long getTotalEmployees() {
        return employeeRepository.count();
    }
}
