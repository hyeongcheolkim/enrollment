package com.khc.enrollment.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminRegisterRequest {
    @NotBlank
    private String loginId;
    @NotBlank
    private String pw;
    @NotBlank
    private String name;
}
