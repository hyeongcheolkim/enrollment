package com.khc.enrollment.service;

import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.service.dto.SubjectMakeDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SubjectServiceTest {

    @Autowired
    private SubjectService subjectService;

    @Test
    void 과목코드는_중복될_수_없다() {
        SubjectMakeDTO subjectMakeDTO1 = SubjectMakeDTO.builder().code(300).build();
        SubjectMakeDTO subjectMakeDTO2 = SubjectMakeDTO.builder().code(300).build();

        subjectService.make(subjectMakeDTO1);

        Assertions.assertThatThrownBy(() -> subjectService.make(subjectMakeDTO2))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("과목코드가 중복됩니다");
    }
}