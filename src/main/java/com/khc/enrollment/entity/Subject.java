package com.khc.enrollment.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject  {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer credit;

    private Integer code;

    @Builder.Default
    @Setter
    private Boolean activated = true;

    @Enumerated(EnumType.STRING)
    private SubjectType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Subject subject = (Subject) o;
        return Objects.equals(code, subject.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}