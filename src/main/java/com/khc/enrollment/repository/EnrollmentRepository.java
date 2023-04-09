package com.khc.enrollment.repository;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Enrollment;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.entity.member.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Long countAllByCourse(Course course);

    List<Enrollment> findAllByStudent(Student student);

    Page<Enrollment> findAllByStudentAndOnSemesterFalse(Student student, Pageable pageable);

    Page<Enrollment> findAllByStudentAndOnSemesterTrue(Student student, Pageable pageable);

    @Query("select e from Enrollment e join e.course c where c.professor = :professor and e.onSemester = TRUE")
    Page<Enrollment> findAllByProfessorOnSemesterTrue(@Param("professor") Professor professor, Pageable pageable);
}
