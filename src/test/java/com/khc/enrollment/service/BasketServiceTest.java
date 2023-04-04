package com.khc.enrollment.service;

import com.khc.enrollment.entity.*;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Course.Day;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.repository.ClassroomRepository;
import com.khc.enrollment.repository.EnrollmentRepository;
import com.khc.enrollment.repository.SubjectRepository;
import com.khc.enrollment.service.dto.CourseOpenDTO;
import com.khc.enrollment.service.dto.CreateDepartmentDTO;
import com.khc.enrollment.service.dto.ProfessorRegisterDTO;
import com.khc.enrollment.service.dto.StudentRegisterDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BasketServiceTest {

    @Autowired
    StudentService studentService;
    @Autowired
    CourseService courseService;
    @Autowired
    EnrollmentService enrollmentService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    ClassroomRepository classroomRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ProfessorService professorService;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    BasketService basketService;

    @Test
    void 과목코드가_같은_코스는_장바구니에_중복으로_담을_수_없다(){
        final CreateDepartmentDTO createDepartmentDTO = CreateDepartmentDTO.builder().build();
        final Department department = departmentService.create(createDepartmentDTO);

        final StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .originDepartment(department)
                .build();
        Student student = studentService.register(studentRegisterDTO);

        final Professor professor = professorService.register(new ProfessorRegisterDTO());

        final Classroom classroom1 = Classroom.builder()
                .name("정보기술관")
                .code(200)
                .build();
        final Classroom classroom2 = Classroom.builder()
                .name("백주년기념관")
                .code(300)
                .build();
        classroomRepository.saveAll(List.of(classroom1, classroom2));

        final Subject subject1 = Subject.builder()
                .credit(3)
                .code(300)
                .name("C++")
                .build();
        subjectRepository.save(subject1);

        final CourseOpenDTO courseOpenDTO1 = CourseOpenDTO.builder()
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(9)
                        .endHour(13)
                        .build()))
                .subject(subject1)
                .professor(professor)
                .classroom(classroom1)
                .capacity(3)
                .division(1)
                .allowedDepartments(List.of(department))
                .build();
        final CourseOpenDTO courseOpenDTO2 = CourseOpenDTO.builder()
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(8)
                        .endHour(15)
                        .build()))
                .subject(subject1)
                .professor(professor)
                .classroom(classroom2)
                .capacity(3)
                .division(1)
                .allowedDepartments(List.of(department))
                .build();

        final Course course1 = courseService.open(courseOpenDTO1);
        final Course course2 = courseService.open(courseOpenDTO2);

        basketService.put(student, course1);
        Assertions.assertThatThrownBy(() -> basketService.put(student, course2))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("Entity가 중복입니다");
    }
}