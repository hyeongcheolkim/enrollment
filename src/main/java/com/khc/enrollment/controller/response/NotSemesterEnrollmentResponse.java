package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Enrollment;
import com.khc.enrollment.entity.ScoreType;
import lombok.Data;

@Data
public class NotSemesterEnrollmentResponse {
    private Long enrollmentId;
    private Long subjectId;

    private Integer subjectCode;
    private Integer subjectCredit;
    private Integer division;
    private Integer courseOpenYear;
    private Integer courseOpenSemester;

    private ScoreType scoreType;
    private String subjectName;

    public NotSemesterEnrollmentResponse(Enrollment enrollment) {
        this.enrollmentId = enrollment.getId();
        this.subjectId = enrollment.getCourse().getSubject().getId();
        this.subjectCode = enrollment.getCourse().getSubject().getCode();
        this.subjectCredit = enrollment.getCourse().getSubject().getCredit();
        this.division = enrollment.getCourse().getDivision();
        this.courseOpenYear = enrollment.getCourse().getOpenYear();
        this.courseOpenSemester = enrollment.getCourse().getOpenSemester();
        this.scoreType = enrollment.getScoreType();
        this.subjectName = enrollment.getCourse().getSubject().getName();
    }
}
