package com.example.employee_management.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.employee_management.dto.DepartmentDTO;
import com.example.employee_management.models.Department;
import com.example.employee_management.repositories.DepartmentRepository;

@Service
public class DepartmentService extends AbstractService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(ModelMapper modelMapper, DepartmentRepository departmentRepository) {
        super(modelMapper);
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream()
                .map(department -> convertToDto(department, DepartmentDTO.class))
                .toList();
    }

    public DepartmentDTO create(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO, Department.class);

        Department savedDepartment = departmentRepository.save(department);

        return convertToDto(savedDepartment, DepartmentDTO.class);
    }

    public DepartmentDTO update(Long id, DepartmentDTO departmentDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (departmentDTO.getName() != null) {
            existingDepartment.setName(departmentDTO.getName());
        }

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return convertToDto(updatedDepartment, DepartmentDTO.class);
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
