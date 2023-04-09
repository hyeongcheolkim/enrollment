package com.khc.enrollment.entity;

import com.khc.enrollment.entity.Course.Course;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer code;

    @Builder.Default
    @Setter
    private Boolean activated = true;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classroom")
    @Builder.Default
    List<Course> courses = new ArrayList<>();
}
