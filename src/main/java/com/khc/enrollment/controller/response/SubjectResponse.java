package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.entity.SubjectType;
import lombok.Data;

@Data
public class SubjectResponse {

    private Long subjectId;

    private String subjectName;

    private Integer credit;

    private Integer code;

    private SubjectType type;

    public SubjectResponse(Subject subject){
        this.subjectId = subject.getId();
        this.subjectName = subject.getName();
        this.credit = subject.getCredit();
        this.code = subject.getCode();
        this.type = subject.getType();
    }
}
