package com.khc.enrollment.repository;

import com.khc.enrollment.entity.Classroom;
import com.khc.enrollment.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    List<Classroom> findAllByCode(Integer code);
}
