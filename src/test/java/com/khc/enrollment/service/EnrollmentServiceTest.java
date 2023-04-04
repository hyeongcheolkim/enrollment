package com.khc.enrollment.service;

import com.khc.enrollment.entity.*;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Course.Day;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.NotAuthenticatedException;
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
class EnrollmentServiceTest {

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

    @Test
    void 코스수강정원이_꽉찬_코스는_수강신청할_수_없다() {
        final CreateDepartmentDTO createDepartmentDTO = CreateDepartmentDTO.builder().build();
        final Department department = departmentService.create(createDepartmentDTO);

        final CourseOpenDTO courseOpenDTO = CourseOpenDTO.builder()
                .capacity(2)
                .allowedDepartments(List.of(department))
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(8)
                        .endHour(15)
                        .build()))
                .build();
        final StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .originDepartment(department)
                .build();

        Student student1 = studentService.register(studentRegisterDTO);
        Student student2 = studentService.register(studentRegisterDTO);
        Student student3 = studentService.register(studentRegisterDTO);
        Course course = courseService.open(courseOpenDTO);

        enrollmentService.enroll(student1, course);
        enrollmentService.enroll(student2, course);

        Assertions.assertThatThrownBy(() -> enrollmentService.enroll(student3, course))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("코스 인원이 꽉 찼습니다. 수강신청할 수 없습니다");
    }

    @Test
    void 수강_금지_학과는_수강신청자격이_없다() {
        final CreateDepartmentDTO createDepartmentDTO = CreateDepartmentDTO.builder().build();
        final Department department = departmentService.create(createDepartmentDTO);

        final CourseOpenDTO courseOpenDTO = CourseOpenDTO.builder()
                .capacity(2)
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(8)
                        .endHour(15)
                        .build()))
                .prohibitedMajorTypes(List.of(MajorType.ORIGIN))
                .build();
        final StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .originDepartment(department)
                .build();

        Student student = studentService.register(studentRegisterDTO);
        Course course = courseService.open(courseOpenDTO);

        Assertions.assertThatThrownBy(() -> enrollmentService.enroll(student, course))
                .isInstanceOf(NotAuthenticatedException.class)
                .hasMessage("수강 신청 자격이 없습니다");
    }

    @Test
    void 기존_수강중_과목과_시간이_겹치는_과목은_수강신청불가() {
        final CreateDepartmentDTO createDepartmentDTO = CreateDepartmentDTO.builder().build();
        final Department department = departmentService.create(createDepartmentDTO);
        final StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .originDepartment(department)
                .build();
        Student student = studentService.register(studentRegisterDTO);
        final Classroom classroom1 = Classroom.builder()
                .name("정보기술관")
                .code(200)
                .build();
        final Classroom classroom2 = Classroom.builder()
                .name("백주년기념관")
                .code(300)
                .build();
        classroomRepository.saveAll(List.of(classroom1, classroom2));

        final CourseOpenDTO courseOpenDTO1 = CourseOpenDTO.builder()
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(9)
                        .endHour(13)
                        .build()))
                .classroom(classroom1)
                .capacity(3)
                .allowedDepartments(List.of(department))
                .build();
        final CourseOpenDTO courseOpenDTO2 = CourseOpenDTO.builder()
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(8)
                        .endHour(15)
                        .build()))
                .classroom(classroom2)
                .capacity(3)
                .allowedDepartments(List.of(department))
                .build();

        final Course course1 = courseService.open(courseOpenDTO1);
        final Course course2 = courseService.open(courseOpenDTO2);

        enrollmentService.enroll(student, course1);

        Assertions.assertThatThrownBy(() -> enrollmentService.enroll(student, course2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("신청한 과목의 수업시간이 기존 시간표와 중복됩니다");
    }

    @Test
    void 이미_수강한_과목은_다시_신청할_수_없다() {
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
                .allowedDepartments(List.of(department))
                .build();

        final Course course1 = courseService.open(courseOpenDTO1);
        final Course course2 = courseService.open(courseOpenDTO2);

        Enrollment enroll1 = enrollmentService.enroll(student, course1);
        enrollmentService.grade(professor, enroll1, ScoreType.B_ZERO);

        Assertions.assertThatThrownBy(() -> enrollmentService.enroll(student, course2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 수강한 과목입니다, 재수강은 B0 미만만 가능합니다");
    }

    @Test
    void 성적이_B0미만이면_다시_신청할_수_있다() {
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
                .allowedDepartments(List.of(department))
                .build();

        final Course course1 = courseService.open(courseOpenDTO1);
        final Course course2 = courseService.open(courseOpenDTO2);

        Enrollment enroll1 = enrollmentService.enroll(student, course1);
        enrollmentService.grade(professor, enroll1, ScoreType.C_ZERO);

        enrollmentService.enroll(student, course2);
    }

    @Test
    void 재수강_채점시_과거수강내역은_삭제된다() {
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
                .allowedDepartments(List.of(department))
                .build();

        final Course course1 = courseService.open(courseOpenDTO1);
        final Course course2 = courseService.open(courseOpenDTO2);

        Enrollment enroll1 = enrollmentService.enroll(student, course1);
        enrollmentService.grade(professor, enroll1, ScoreType.C_ZERO);
        final Long enroll1Id = enroll1.getId();

        Enrollment enroll2 = enrollmentService.enroll(student, course2);
        enrollmentService.grade(professor, enroll2, ScoreType.A_ZERO);

        Optional<Enrollment> res = enrollmentRepository.findById(enroll1Id);
        Assertions.assertThat(res).isEmpty();
    }
}