package com.khc.enrollment.repository;

import com.khc.enrollment.entity.Basket;
import com.khc.enrollment.entity.member.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Page<Basket> findAllByStudent(Student student, Pageable pageable);
}
