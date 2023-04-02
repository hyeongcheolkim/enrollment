package com.khc.enrollment.repository;

import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.entity.member.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    @Query("select p from Professor p where p.memberInfo.loginId = :loginId")
    List<Professor> findAllByLoginId(@Param("loginId") String loginId);
}
