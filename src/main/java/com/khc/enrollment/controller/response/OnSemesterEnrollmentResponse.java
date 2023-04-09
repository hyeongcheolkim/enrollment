package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Enrollment;
import lombok.Data;

@Data
public class OnSemesterEnrollmentResponse {

    private Long enrollmentId;

    private Long subjectId;

    private String subjectName;

    private Integer subjectCode;

    private Integer division;

    private String studentName;

    private String studentDepartmentName;

    public OnSemesterEnrollmentResponse(Enrollment enrollment){
        this.enrollmentId = enrollment.getId();
        this.subjectId = enrollment.getCourse().getSubject().getId();
        this.subjectName = enrollment.getCourse().getSubject().getName();
        this.subjectCode = enrollment.getCourse().getSubject().getCode();
        this.division = enrollment.getCourse().getDivision();
        this.studentName = enrollment.getStudent().getMemberInfo().getName();
        this.studentDepartmentName = enrollment.getStudent().getDepartment().getName();
    }
}
