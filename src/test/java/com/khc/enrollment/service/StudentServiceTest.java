package com.khc.enrollment.service;

import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.MajorType;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.repository.StudentRepository;
import com.khc.enrollment.service.dto.CreateDepartmentDTO;
import com.khc.enrollment.service.dto.ModifyMajorDTO;
import com.khc.enrollment.service.dto.StudentRegisterDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentServiceTest {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    StudentService studentService;

    @Test
    void 학생_전공_변경() {
        final CreateDepartmentDTO dto1 = CreateDepartmentDTO.builder()
                .code(300)
                .name("컴퓨터과학부")
                .build();
        final CreateDepartmentDTO dto2 = CreateDepartmentDTO.builder()
                .code(301)
                .name("기계정보공학과")
                .build();
        final CreateDepartmentDTO dto3 = CreateDepartmentDTO.builder()
                .code(302)
                .name("전기전자컴퓨터")
                .build();

        final Department dept1 = departmentService.create(dto1);
        final Department dept2 = departmentService.create(dto2);
        final Department dept3 = departmentService.create(dto3);

        final StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .originDepartment(dept1)
                .build();

        final Student student = studentService.register(studentRegisterDTO);

        Assertions.assertThat(student.getMajors())
                .containsEntry(MajorType.ORIGIN, dept1);

        final ModifyMajorDTO modifyMajorDTO = ModifyMajorDTO.builder()
                .majors(Map.of(MajorType.ORIGIN, dept2, MajorType.SUB, dept3))
                .build();
        studentService.modifyMajor(student, modifyMajorDTO);

        Assertions.assertThat(student.getMajors())
                .containsEntry(MajorType.ORIGIN, dept2)
                .containsEntry(MajorType.SUB, dept3);
    }

    @Test
    void 동일한학과에대해_여러유형의_전공을_진행할_수_없다(){
        final CreateDepartmentDTO dto1 = CreateDepartmentDTO.builder()
                .code(300)
                .name("컴퓨터과학부")
                .build();
        final Department dept1 = departmentService.create(dto1);

        final StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .originDepartment(dept1)
                .build();
        final Student student = studentService.register(studentRegisterDTO);

        final ModifyMajorDTO modifyMajorDTO = ModifyMajorDTO.builder()
                .majors(Map.of(MajorType.ORIGIN, dept1, MajorType.SUB, dept1))
                .build();

        Assertions.assertThatThrownBy(() -> studentService.modifyMajor(student, modifyMajorDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("동일한 과를 여러 유형으로 전공할 수 없습니다");
    }

    @Test
    void 전공변경은_항상_본전공을_포험해야한다(){
        final CreateDepartmentDTO dto1 = CreateDepartmentDTO.builder()
                .code(300)
                .name("컴퓨터과학부")
                .build();
        final CreateDepartmentDTO dto2 = CreateDepartmentDTO.builder()
                .code(301)
                .name("기계정보공학과")
                .build();
        final Department dept1 = departmentService.create(dto1);
        final Department dept2 = departmentService.create(dto2);

        final StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .originDepartment(dept1)
                .build();
        final Student student = studentService.register(studentRegisterDTO);

        final ModifyMajorDTO modifyMajorDTO = ModifyMajorDTO.builder()
                .majors(Map.of(MajorType.MULTI, dept1, MajorType.SUB, dept2))
                .build();

        Assertions.assertThatThrownBy(() -> studentService.modifyMajor(student, modifyMajorDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("전공변경 신청은 적어도 본전공 정보만큼은 포함해야 합니다");
    }
}