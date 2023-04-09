package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Classroom;
import lombok.Data;

@Data
public class ClassroomResponse {

    private Long classroomId;
    private String classroomName;
    private Integer classroomCode;
    public ClassroomResponse(Classroom classroom) {
        this.classroomCode = classroom.getCode();
        this.classroomName = classroom.getName();
        this.classroomId = classroom.getId();
    }
}
