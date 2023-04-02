package com.khc.enrollment.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateDepartmentRequest {
    @NotBlank
    private String name;
    @NotNull
    private Integer code;
}
