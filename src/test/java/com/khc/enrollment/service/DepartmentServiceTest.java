package com.khc.enrollment.service;

import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.service.dto.CreateDepartmentDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
    void 학과코드는_중복될_수_없다() {
        CreateDepartmentDTO createDepartmentDTO1 = CreateDepartmentDTO.builder().code(300).build();
        CreateDepartmentDTO createDepartmentDTO2 = CreateDepartmentDTO.builder().code(300).build();

        departmentService.create(createDepartmentDTO1);

        Assertions.assertThatThrownBy(() -> departmentService.create(createDepartmentDTO2))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("학과 코드가 중복됩니다");
    }
}