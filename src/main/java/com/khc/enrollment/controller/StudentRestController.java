package com.khc.enrollment.controller;

import com.khc.enrollment.aop.annotation.PermitStudent;
import com.khc.enrollment.controller.request.StudentRegisterRequest;
import com.khc.enrollment.controller.response.BasketResponse;
import com.khc.enrollment.controller.response.LoginResponse;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.BasketRepository;
import com.khc.enrollment.repository.DepartmentRepository;
import com.khc.enrollment.repository.StudentRepository;
import com.khc.enrollment.service.StudentService;
import com.khc.enrollment.service.dto.StudentRegisterDTO;
import com.khc.enrollment.session.SessionConst;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentRestController {

    private final StudentService studentService;

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final BasketRepository basketRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Valid
    ResponseEntity<LoginResponse> login(
            @RequestParam @NotBlank String loginId,
            @RequestParam @NotBlank String pw,
            HttpServletRequest request,
            HttpServletResponse response) {
        Student student = studentService.login(loginId, pw).orElseThrow(NoExistEntityException::new);

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_STUDENT, student.getId());
        session.setMaxInactiveInterval(1800);
        log.info("Session Create [SessionId : {}]", session.getId());

        response.addCookie(new Cookie("JSESSIONID", session.getId()));

        return ResponseEntity.ok(LoginResponse.builder()
                .id(student.getId())
                .type(SessionConst.LOGIN_STUDENT)
                .name(student.getMemberInfo().getName())
                .departmentName(student.getDepartment().getName())
                .build());
    }

    @PostMapping("/logout")
    void logout(HttpServletRequest request) {
        logoutStudent(request);
    }

    private void logoutStudent(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return;
        session.invalidate();
    }

    @PostMapping("/register")
    void register(@RequestBody @Valid StudentRegisterRequest studentRegisterRequest) {
        String encodedPw = passwordEncoder.encode(studentRegisterRequest.getPw());
        studentRegisterRequest.setPw(encodedPw);

        StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .name(studentRegisterRequest.getName())
                .pw(studentRegisterRequest.getPw())
                .loginId(studentRegisterRequest.getLoginId())
                .build();

        studentService.register(studentRegisterDTO);
    }

    @PermitStudent
    @PostMapping("/inactive")
    @Valid
    void inactiveSubject(@Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_STUDENT, required = false) Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(NoExistEntityException::new);
        studentService.inactive(student);
    }

    @PermitStudent
    @PostMapping("/basket")
    Page<BasketResponse> baskets(
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_STUDENT, required = false) Long studentId,
            Pageable pageable) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(NoExistEntityException::new);

        return basketRepository.findAllByStudent(student, pageable)
                .map(BasketResponse::new);
    }
}