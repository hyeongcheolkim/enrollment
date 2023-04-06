package com.khc.enrollment.repository;

import com.khc.enrollment.entity.member.Admin;
import com.khc.enrollment.entity.member.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("select a from Admin a where a.memberInfo.loginId = :loginId")
    List<Admin> findAllByLoginId(@Param("loginId") String loginId);
}
