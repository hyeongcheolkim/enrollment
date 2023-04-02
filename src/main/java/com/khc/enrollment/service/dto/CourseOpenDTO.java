package com.khc.enrollment.service.dto;

import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.MajorType;
import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.entity.member.Professor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseOpenDTO {
    private Subject subject;

    private Department department;

    private Integer capacity;

    private Integer studentYear;

    private Integer openYear;

    private Integer openSemester;

    private Integer division;

    private Professor professor;

    private List<CourseTime> courseTimes;

    private List<Department> prohibitedDepartments;

    private List<MajorType> prohibitedMajorTypes;
}
