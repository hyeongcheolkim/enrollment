package com.khc.enrollment.controller.request;

import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.entity.SubjectType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SubjectMakeRequest {
    @NotBlank
    private String name;

    @NotNull
    private Integer credit;

    @NotNull
    private Integer code;

    @NotNull
    private Long prerequisiteId;

    @NotNull
    private SubjectType type;
}
