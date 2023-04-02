package com.khc.enrollment.repository;

import com.khc.enrollment.entity.ParticipatedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipatedCourseRepository extends JpaRepository<ParticipatedCourse, Long> {
}
