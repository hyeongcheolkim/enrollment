package com.khc.enrollment.controller.request;

import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.MajorType;
import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.service.dto.CourseOpenDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CourseOpenRequest {
    @NotNull
    private Long subjectId;

    @NotNull
    private Long departmentId;

    @NotNull
    private Integer capacity;

    @NotNull
    private Integer studentYear;

    @NotNull
    private Integer openYear;

    @NotNull
    private Integer openSemester;

    @NotNull
    private Integer division;

    @NotNull
    private Long professorId;

    @Size(min = 1)
    private List<CourseTime> courseTimes;

    @Nullable
    private List<Long> prohibitedDepartmentIds;

    @Nullable
    private List<MajorType> prohibitedMajorTypes;
}
