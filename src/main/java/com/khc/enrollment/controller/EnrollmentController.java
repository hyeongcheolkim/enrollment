package com.khc.enrollment.controller;

import com.khc.enrollment.controller.response.GradedEnrollmentResponse;
import com.khc.enrollment.controller.response.OnSemesterEnrollmentResponse;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Enrollment;
import com.khc.enrollment.entity.ScoreType;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.CourseRepository;
import com.khc.enrollment.repository.EnrollmentRepository;
import com.khc.enrollment.repository.StudentRepository;
import com.khc.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/enroll")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @PostMapping("/enroll")
    @Valid
    public void enroll(
            @RequestParam @NotNull Long studentId,
            @RequestParam @NotNull Long courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow(NoExistEntityException::new);
        Course course = courseRepository.findById(courseId).orElseThrow(NoExistEntityException::new);

        enrollmentService.enroll(student, course);
    }

    @PostMapping("/drop")
    @Valid
    public void drop(@RequestParam @NotNull Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(NoExistEntityException::new);

        enrollmentService.drop(enrollment);
    }

    @PostMapping("/grade")
    @Valid
    public void grade(
            @RequestParam @NotNull Long enrollmentId,
            @RequestParam @NotNull ScoreType scoreType) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(NoExistEntityException::new);

        enrollmentService.grade(enrollment, scoreType);
    }

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
