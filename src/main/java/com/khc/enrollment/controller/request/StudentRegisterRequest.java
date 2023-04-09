package com.khc.enrollment.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StudentRegisterRequest {
    @NotBlank
    private String loginId;
    @NotBlank
    private String pw;
    @NotBlank
    private String name;
}
