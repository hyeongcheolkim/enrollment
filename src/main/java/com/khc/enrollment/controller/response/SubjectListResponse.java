package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Subject;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SubjectListResponse {
    List<SubjectResponse> subjects;

    public SubjectListResponse(List<Subject> subjects) {
        this.subjects = subjects.stream()
                .map(SubjectResponse::new)
                .collect(Collectors.toList());
    }
}
