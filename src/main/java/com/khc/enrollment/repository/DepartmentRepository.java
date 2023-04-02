package com.khc.enrollment.repository;

import com.khc.enrollment.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByCode(Integer code);
}
