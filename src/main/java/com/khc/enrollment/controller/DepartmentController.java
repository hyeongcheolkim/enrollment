package com.khc.enrollment.controller;

import com.khc.enrollment.controller.request.CreateDepartmentRequest;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.DepartmentRepository;
import com.khc.enrollment.service.DepartmentService;
import com.khc.enrollment.service.dto.CreateDepartmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;


    @PostMapping("/create")
    public void createDepartment(@RequestBody @Valid CreateDepartmentRequest createDepartmentRequest){
        CreateDepartmentDTO createDepartmentDTO =
                modelMapper.map(createDepartmentRequest, CreateDepartmentDTO.class);

        departmentService.create(createDepartmentDTO);
    }

    @PostMapping("/inactivate")
    @Valid
    public void inactivateDepartment(@RequestParam @NotNull Long departmentId){
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(NoExistEntityException::new);

        departmentService.inactive(department);
    }
}
