package com.example.employee_management.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final ModelMapper modelMapper;

    public EmployeeService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
