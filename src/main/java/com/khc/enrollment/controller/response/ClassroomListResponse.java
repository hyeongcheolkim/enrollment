package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Classroom;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClassroomListResponse {
    List<ClassroomResponse> classrooms;

    public ClassroomListResponse(List<Classroom> classrooms) {
        this.classrooms = classrooms.stream()
                .map(ClassroomResponse::new)
                .collect(Collectors.toList());
    }
}
