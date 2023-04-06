package com.khc.enrollment.controller;

import com.khc.enrollment.aop.annotation.PermitAnyLogin;
import com.khc.enrollment.aop.annotation.PermitProfessor;
import com.khc.enrollment.aop.annotation.PermitStudent;
import com.khc.enrollment.controller.response.GradedEnrollmentResponse;
import com.khc.enrollment.controller.response.OnSemesterEnrollmentResponse;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Enrollment;
import com.khc.enrollment.entity.ScoreType;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.CourseRepository;
import com.khc.enrollment.repository.EnrollmentRepository;
import com.khc.enrollment.repository.ProfessorRepository;
import com.khc.enrollment.repository.StudentRepository;
import com.khc.enrollment.service.EnrollmentService;
import com.khc.enrollment.session.SessionConst;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/enroll")
@RequiredArgsConstructor
@Transactional
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;

    @PermitStudent
    @PostMapping("/enroll")
    @Valid
    public void enroll(
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_STUDENT) Long studentId,
            @RequestParam @NotNull Long courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow(NoExistEntityException::new);
        Course course = courseRepository.findById(courseId).orElseThrow(NoExistEntityException::new);

        enrollmentService.enroll(student, course);
    }

    @PermitStudent
    @PostMapping("/drop")
    @Valid
    public void drop(
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_STUDENT) Long studentId,
            @RequestParam @NotNull Long enrollmentId
    ) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(NoExistEntityException::new);
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(NoExistEntityException::new);

        enrollmentService.drop(student, enrollment);
    }

    @PermitProfessor
    @PostMapping("/grade")
    @Valid
    public void grade(
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_PROFESSOR) Long professorId,
            @RequestParam @NotNull Long enrollmentId,
            @RequestParam @NotNull ScoreType scoreType) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(NoExistEntityException::new);
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(NoExistEntityException::new);

        enrollmentService.grade(professor, enrollment, scoreType);
    }

    @PermitAnyLogin
    @GetMapping("/score")
    @Valid
    public Page<GradedEnrollmentResponse> gradedEnrollments(
            @RequestParam @NotNull Long studentId,
            Pageable pageable
    ) {
        Student student = studentRepository.findById(studentId).orElseThrow(NoExistEntityException::new);

        return enrollmentRepository.findAllByStudentAndOnSemesterFalse(student, pageable)
                .map(GradedEnrollmentResponse::new);
    }

    @PermitAnyLogin
    @GetMapping("/on-semester")
    public Page<OnSemesterEnrollmentResponse> onSemesterEnrollments(
            @RequestParam @NotNull Long studentId,
            Pageable pageable
    ) {
        Student student = studentRepository.findById(studentId).orElseThrow(NoExistEntityException::new);

        return enrollmentRepository.findAllByStudentAndOnSemesterTrue(student, pageable)
                .map(OnSemesterEnrollmentResponse::new);
    }
}
