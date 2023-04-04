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

    @ManyToMany(fetch = FetchType.LAZY)
    @Setter
    @Builder.Default
    private Map<MajorType, Department> majors = new HashMap<>();

    @OneToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Basket> baskets = new ArrayList<>();
}
