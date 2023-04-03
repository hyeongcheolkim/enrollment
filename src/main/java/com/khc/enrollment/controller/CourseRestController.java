package com.khc.enrollment.controller;

import com.khc.enrollment.controller.request.CourseOpenRequest;
import com.khc.enrollment.controller.response.CourseResponse;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.CourseRepository;
import com.khc.enrollment.repository.DepartmentRepository;
import com.khc.enrollment.repository.ProfessorRepository;
import com.khc.enrollment.repository.SubjectRepository;
import com.khc.enrollment.service.CourseService;
import com.khc.enrollment.service.dto.CourseOpenDTO;
import com.khc.enrollment.session.SessionConst;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Transactional
public class CourseRestController {

    private final CourseService courseService;

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final ProfessorRepository professorRepository;

    @PostMapping("/open")
    public void openCourse(
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGINP_PROFESSOR) Long professorId,
            @RequestBody @Valid CourseOpenRequest courseOpenRequest
    ) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(NoExistEntityException::new);
        Subject subject = subjectRepository.findById(courseOpenRequest.getSubjectId())
                .orElseThrow(NoExistEntityException::new);
        Department department = departmentRepository.findById(courseOpenRequest.getDepartmentId())
                .orElseThrow(NoExistEntityException::new);
        List<Department> allowedDepartments = courseOpenRequest.getAllowedDepartmentIds().stream()
                .map(e -> departmentRepository.findById(e).orElseThrow(NoExistEntityException::new))
                .collect(Collectors.toList());

        CourseOpenDTO courseOpenDTO = CourseOpenDTO.builder()
                .subject(subject)
                .department(department)
                .professor(professor)
                .allowedDepartments(allowedDepartments)
                .courseTimes(courseOpenRequest.getCourseTimes())
                .capacity(courseOpenRequest.getCapacity())
                .openSemester(courseOpenRequest.getOpenSemester())
                .openYear(courseOpenRequest.getOpenYear())
                .division(courseOpenRequest.getDivision())
                .studentYear(courseOpenRequest.getStudentYear())
                .prohibitedMajorTypes(courseOpenRequest.getProhibitedMajorTypes())
                .build();

        courseService.open(courseOpenDTO);
    }

    @PostMapping("/close")
    @Valid
    public void close(
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGINP_PROFESSOR) Long professorId,
            @RequestParam @NotNull Long courseId
    ){
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(NoExistEntityException::new);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(NoExistEntityException::new);

        courseService.close(course, professor);
    }

    @GetMapping("/list")
    public Page<CourseResponse> courseList(Pageable pageable){
        return courseRepository.findAll(pageable)
                .map(CourseResponse::new);
    }
}
