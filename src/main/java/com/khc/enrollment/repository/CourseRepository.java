package com.khc.enrollment.repository;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.Day;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select c from Course c join c.courseTimes t where t.day = :day")
    List<Course> findAllCourseByDay(@Param("day") Day day);

    Page<Course> findAll(Pageable pageable);
}
