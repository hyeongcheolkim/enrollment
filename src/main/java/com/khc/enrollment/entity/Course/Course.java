package com.khc.enrollment.entity.Course;

import com.khc.enrollment.entity.*;
import com.khc.enrollment.entity.member.Professor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @ManyToOne
    @JoinColumn
    private Department department;

    private Integer capacity;

    private Integer studentYear;

    private Integer openYear;

    private Integer openSemester;

    private Integer division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Professor professor;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<CourseTime> courseTimes;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Department> allowedDepartments;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<MajorType> prohibitedMajorTypes;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;
}
