package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Enrollment;
import lombok.Data;

@Data
public class OnSemesterEnrollmentResponse {

    private Long subjectId;

    private String subjectName;

    private Integer code;

    public OnSemesterEnrollmentResponse(Enrollment enrollment){
        subjectId = enrollment.getCourse().getSubject().getId();
        subjectName = enrollment.getCourse().getSubject().getName();
        code = enrollment.getCourse().getSubject().getCode();
    }
}
