package com.khc.enrollment.entity;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.member.Student;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment  {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Student student;

    @Setter
    private boolean onSemester = true;

    @Setter
    private ScoreType score;

    @Builder
    private Enrollment(Course course, Student student) {
        this.course = course;
        this.student = student;
        course.getEnrollments().add(this);
        student.getEnrollments().add(this);
    }
}
