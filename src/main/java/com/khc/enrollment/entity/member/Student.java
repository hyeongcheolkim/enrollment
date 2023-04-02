package com.khc.enrollment.entity.member;

import com.khc.enrollment.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Base {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private MemberInfo memberInfo;

    @OneToMany(fetch = FetchType.LAZY)
    private Map<MajorType, Department> majors;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ParticipatedCourse> participatedCourse;

    @OneToMany(fetch = FetchType.LAZY)
    private List<GradeCard> gradeCards;
}
