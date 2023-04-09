package com.khc.enrollment.entity.member;

import com.khc.enrollment.entity.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student  {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private MemberInfo memberInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Department department;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    @Builder.Default
    private List<Basket> baskets = new ArrayList<>();
}
