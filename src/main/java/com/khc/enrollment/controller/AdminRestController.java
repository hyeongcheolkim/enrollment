package com.khc.enrollment.controller;

import com.khc.enrollment.aop.annotation.PermitAdmin;
import com.khc.enrollment.controller.request.AdminRegisterRequest;
import com.khc.enrollment.controller.response.LoginResponse;
import com.khc.enrollment.entity.member.Admin;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.AdminRepository;
import com.khc.enrollment.service.AdminService;
import com.khc.enrollment.service.dto.AdminRegisterDTO;
import com.khc.enrollment.session.SessionConst;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminRestController {

    private final AdminService adminService;

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    @Valid
    ResponseEntity<LoginResponse> login(
            @RequestParam @NotBlank String loginId,
            @RequestParam @NotBlank String pw,
            HttpServletRequest request) {
        Admin admin = adminService.login(loginId, pw).orElseThrow(NoExistEntityException::new);

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_ADMIN, admin.getId());
        session.setMaxInactiveInterval(1800);
        log.info("Admin Session Create [SessionId : {}]", session.getId());

        return ResponseEntity.ok(LoginResponse.builder()
                .id(admin.getId())
                .type(SessionConst.LOGIN_ADMIN)
                .name(admin.getMemberInfo().getName())
                .build());
    }

    @PostMapping("/logout")
    void logout(HttpServletRequest request) {
        logoutAdmin(request);
    }

    private void logoutAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return;
        session.invalidate();
    }

    @PostMapping("/register")
    void register(@RequestBody @Valid AdminRegisterRequest adminRegisterRequest) {
        String encodedPw = passwordEncoder.encode(adminRegisterRequest.getPw());
        adminRegisterRequest.setPw(encodedPw);

        AdminRegisterDTO adminRegisterDTO = modelMapper.map(adminRegisterRequest, AdminRegisterDTO.class);
        adminService.register(adminRegisterDTO);
    }

    @PermitAdmin
    @PostMapping("/inactive")
    @Valid
    void inactiveSubject(@Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_PROFESSOR, required = false) Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(NoExistEntityException::new);
        adminService.inactive(admin);
    }
}
