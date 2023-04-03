package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Enrollment;
import lombok.Data;

@Data
public class GradedEnrollmentResponse {

    private Long subjectId;

    private String subjectName;

    private Integer credit;

    private Double score;

    private Integer code;

    public GradedEnrollmentResponse(Enrollment enrollment){
        subjectId = enrollment.getCourse().getSubject().getId();
        subjectName = enrollment.getCourse().getSubject().getName();
        credit = enrollment.getCourse().getSubject().getCredit();
        score = enrollment.getScore().getDigit();
        code = enrollment.getCourse().getSubject().getCode();
    }
}
