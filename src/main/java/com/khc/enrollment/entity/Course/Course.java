package com.khc.enrollment.entity.Course;

import com.khc.enrollment.entity.*;
import com.khc.enrollment.entity.member.Professor;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JoinColumn
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Department department;

    private Integer capacity;

    private Integer studentYear;

    private Integer openYear;

    private Integer openSemester;

    private Integer division;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Professor professor;

    @Builder.Default
    @Setter
    private Boolean activated = true;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<CourseTime> courseTimes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Department> allowedDepartments = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<MajorType> prohibitedMajorTypes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();
}
