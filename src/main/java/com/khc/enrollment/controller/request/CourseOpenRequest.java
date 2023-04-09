package com.khc.enrollment.controller.request;

import com.khc.enrollment.entity.Course.CourseTime;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CourseOpenRequest {
    @NotNull
    private Long subjectId;

    @NotNull
    private Long departmentId;

    @NotNull
    private Integer capacity;

    @Min(value = 1, message = "1~5 숫자만 입력 가능합니다")
    @Max(value = 4, message = "1~5 숫자만 입력 가능합니다")
    @NotNull
    private Integer studentYear;

    @NotNull
    private Integer openYear;

    @Min(value = 1, message = "1 또는 2여야 합니다")
    @Max(value = 2, message = "1 또는 2여야 합니다")
    @NotNull
    private Integer openSemester;

    @NotNull
    private Integer division;

    private Long classroomId;

    @NotNull
    private CourseTime courseTime;

    @Nullable
    private List<Long> prohibitedDepartmentIds;
}
