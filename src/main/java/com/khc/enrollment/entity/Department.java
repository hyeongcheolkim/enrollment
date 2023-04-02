package com.khc.enrollment.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department extends Base {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer code;

    @Builder.Default
    @Setter
    private Boolean activated = true;
}
