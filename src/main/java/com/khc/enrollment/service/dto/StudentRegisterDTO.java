package com.khc.enrollment.service.dto;

import com.khc.enrollment.entity.Department;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentRegisterDTO {
    private String loginId;
    private String pw;
    private String name;
    private Long departmentId;
}
