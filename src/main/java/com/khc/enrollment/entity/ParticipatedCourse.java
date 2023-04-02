package com.khc.enrollment.entity;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.member.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipatedCourse extends Base {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Student student;
}
