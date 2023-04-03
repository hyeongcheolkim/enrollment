package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Department;
import lombok.Builder;
import lombok.Data;

@Data
public class DepartmentResponse {

    private Long departmentId;
    private String departmentName;
    private Integer departmentCode;

    public DepartmentResponse(Department department) {
        this.departmentId = department.getId();
        this.departmentName = department.getName();
        this.departmentCode = department.getCode();
    }
}
