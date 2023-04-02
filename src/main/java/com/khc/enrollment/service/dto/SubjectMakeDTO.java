package com.khc.enrollment.service.dto;

import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.entity.SubjectType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
public class SubjectMakeDTO {
    private String name;

    private Integer credit;

    private Integer code;

    private Subject prerequisite;

    private SubjectType type;
}
