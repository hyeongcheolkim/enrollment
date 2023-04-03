package com.khc.enrollment.service;

import com.khc.enrollment.entity.Department;
import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.repository.DepartmentRepository;
import com.khc.enrollment.service.dto.CreateDepartmentDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public void create(CreateDepartmentDTO createDepartmentDTO){
        if(isDuplicatedDepartment(createDepartmentDTO.getCode()))
            throw new DuplicatedEntityException("학과 코드가 중복됩니다");

        Department department = modelMapper.map(createDepartmentDTO, Department.class);
        departmentRepository.save(department);
    }

    public void inactive(Department department){
        department.setActivated(false);
    }

    private boolean isDuplicatedDepartment(Integer code) {
        return departmentRepository.findAllByCode(code).stream()
                .filter(Department::getActivated)
                .anyMatch(e -> e.getCode().equals(code));
    }
}
