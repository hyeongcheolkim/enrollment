package com.khc.enrollment.controller;

import com.khc.enrollment.controller.request.StudentRegisterRequest;
import com.khc.enrollment.controller.response.LoginResponse;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.DepartmentRepository;
import com.khc.enrollment.repository.StudentRepository;
import com.khc.enrollment.service.StudentService;
import com.khc.enrollment.service.dto.StudentRegisterDTO;
import com.khc.enrollment.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService studentService;

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Valid
    ResponseEntity<LoginResponse> login(
            @RequestParam("loginId") @NotBlank String loginId,
            @RequestParam("pw") @NotBlank String pw,
            HttpServletRequest request) {
        Student student = studentService.login(loginId, pw).orElseThrow(NoExistEntityException::new);

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.loginStudent, student);
        session.setMaxInactiveInterval(1800);

        return ResponseEntity.ok(LoginResponse.builder()
                .id(student.getId())
                .type(SessionConst.loginStudent)
                .build());
    }

    @PostMapping("/logout")
    void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return;
        session.invalidate();
    }

    @PostMapping("/register")
    void register(@RequestBody @Valid StudentRegisterRequest studentRegisterRequest) {
        Department originDepartment = departmentRepository.findById(studentRegisterRequest.getOriginDepartmentId())
                .orElseThrow(NoExistEntityException::new);

        String encodedPw = passwordEncoder.encode(studentRegisterRequest.getPw());
        studentRegisterRequest.setPw(encodedPw);

        StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder()
                .name(studentRegisterRequest.getName())
                .pw(studentRegisterRequest.getPw())
                .loginId(studentRegisterRequest.getLoginId())
                .originDepartment(originDepartment)
                .build();

        studentService.register(studentRegisterDTO);
    }

    @PostMapping("/inactive")
    @Valid
    void inactiveSubject(@RequestParam("studentId") @NotNull Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(NoExistEntityException::new);

        studentService.inactive(student);
    }
}