package com.khc.enrollment.entity.Course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseTime {
    private Day day;
    private Integer startHour;
    private Integer endHour;
}
