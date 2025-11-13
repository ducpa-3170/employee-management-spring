package com.example.employee_management.services;

import org.modelmapper.ModelMapper;

public abstract class AbstractService {
    protected final ModelMapper modelMapper;

    public AbstractService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    protected <D> D convertToDto(Object entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    protected <E> E convertToEntity(Object dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }
}
