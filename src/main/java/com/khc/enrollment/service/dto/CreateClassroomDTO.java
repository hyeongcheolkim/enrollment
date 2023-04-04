package com.khc.enrollment.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateClassroomDTO {
    private String name;
    private Integer code;
}
