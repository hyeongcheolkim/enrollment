package com.khc.enrollment.repository;

import com.khc.enrollment.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByCode(Integer code);
}
