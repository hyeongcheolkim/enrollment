package com.khc.enrollment.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject extends Base {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer credit;

    private Integer code;

    @Builder.Default
    @Setter
    private Boolean activated = true;

    @OneToOne(fetch = FetchType.LAZY)
    private Subject prerequisite;

    @Enumerated(EnumType.STRING)
    private SubjectType type;
}