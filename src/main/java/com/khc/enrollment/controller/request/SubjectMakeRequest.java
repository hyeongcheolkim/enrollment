package com.khc.enrollment.controller.request;

import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.entity.SubjectType;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class SubjectMakeRequest {
    @NotBlank
    private String name;

    @NotNull
    private Integer credit;

    @NotNull
    private Integer code;

    @NotNull
    private SubjectType type;
}
