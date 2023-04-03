package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Department;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DepartmentListResponse {

    List<DepartmentResponse> departments;

    public DepartmentListResponse(List<Department> departments){
        this.departments = departments.stream()
                .map(DepartmentResponse::new)
                .collect(Collectors.toList());
    }
}
