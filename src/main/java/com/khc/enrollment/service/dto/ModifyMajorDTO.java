package com.khc.enrollment.service.dto;

import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.MajorType;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ModifyMajorDTO {
    @Builder.Default
    Map<MajorType, Department> majors = new HashMap<>();
}
