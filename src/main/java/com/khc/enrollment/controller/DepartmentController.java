package com.khc.enrollment.controller;

import com.khc.enrollment.aop.annotation.PermitAdmin;
import com.khc.enrollment.aop.annotation.PermitAnyLogin;
import com.khc.enrollment.aop.annotation.PermitProfessor;
import com.khc.enrollment.aop.annotation.PermitStudent;
import com.khc.enrollment.controller.request.CreateDepartmentRequest;
import com.khc.enrollment.controller.response.DepartmentListResponse;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.DepartmentRepository;
import com.khc.enrollment.service.DepartmentService;
import com.khc.enrollment.service.dto.CreateDepartmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
@Transactional
public class DepartmentController {

    private final DepartmentService departmentService;

    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;

    @PermitAdmin
    @PostMapping("/create")
    public void createDepartment(@RequestBody @Valid CreateDepartmentRequest createDepartmentRequest) {
        CreateDepartmentDTO createDepartmentDTO =
                modelMapper.map(createDepartmentRequest, CreateDepartmentDTO.class);

        departmentService.create(createDepartmentDTO);
    }

    @PermitAdmin
    @PostMapping("/inactivate")
    @Valid
    public void inactivateDepartment(@RequestParam @NotNull Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(NoExistEntityException::new);

        departmentService.inactive(department);
    }

    @PermitAnyLogin
    @GetMapping("/list")
    public ResponseEntity<DepartmentListResponse> departmentList() {
        List<Department> departments = departmentRepository.findAll().stream()
                .filter(Department::getActivated)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new DepartmentListResponse(departments));
    }
}
