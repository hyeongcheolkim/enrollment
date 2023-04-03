package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.MajorType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CourseResponse {

    private Long courseId;

    private Long subjectId;

    private String subjectName;

    private Long departmentId;

    private String departmentName;

    private Integer capacity;

    private Integer studentYear;

    private Integer openYear;

    private Integer openSemester;

    private Integer division;

    private Long professorId;

    private String professorName;

    private List<CourseTime> courseTimes;

    private List<MajorType> prohibitedMajorTypes;

    private List<DepartmentResponse> allowedDepartments;

    public CourseResponse(Course course){
        this.courseId = course.getId();
        this.subjectId = course.getSubject().getId();
        this.subjectName = course.getSubject().getName();
        this.departmentId = course.getDepartment().getId();
        this.departmentName = course.getDepartment().getName();
        this.capacity = course.getCapacity();
        this.studentYear = course.getStudentYear();
        this.openYear = course.getOpenYear();
        this.openSemester = course.getOpenSemester();
        this.division = course.getDivision();
        this.professorId = course.getProfessor().getId();
        this.professorName = course.getProfessor().getMemberInfo().getName();
        this.courseTimes = course.getCourseTimes();
        this.prohibitedMajorTypes = course.getProhibitedMajorTypes();
        this.allowedDepartments = course.getAllowedDepartments().stream()
                .map(DepartmentResponse::new)
                .collect(Collectors.toList());
    }
}
