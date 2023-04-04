package com.khc.enrollment.entity;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.member.Student;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Basket {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private Student student;

    @ManyToOne
    @JoinColumn
    private Course course;

    @Builder
    private Basket(Student student, Course course){
        this.student = student;
        this.course = course;
        student.getBaskets().add(this);
    }
}
