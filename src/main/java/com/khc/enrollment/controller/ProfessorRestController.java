package com.khc.enrollment.controller;

import com.khc.enrollment.controller.request.ProfessorRegisterRequest;
import com.khc.enrollment.controller.response.LoginResponse;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.ProfessorRepository;
import com.khc.enrollment.service.ProfessorService;
import com.khc.enrollment.service.dto.ProfessorRegisterDTO;
import com.khc.enrollment.session.SessionConst;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/professor")
@RequiredArgsConstructor
@Transactional
public class ProfessorRestController {

    private final ProfessorService professorService;

    private final ProfessorRepository professorRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    @Valid
    ResponseEntity<LoginResponse> login(
            @RequestParam @NotBlank String loginId,
            @RequestParam @NotBlank String pw,
            HttpServletRequest request) {
        Professor professor = professorService.login(loginId, pw).orElseThrow(NoExistEntityException::new);

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGINP_PROFESSOR, professor.getId());
        session.setMaxInactiveInterval(1800);

        return ResponseEntity.ok(LoginResponse.builder()
                .id(professor.getId())
                .type(SessionConst.LOGINP_PROFESSOR)
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
    void register(@RequestBody @Valid ProfessorRegisterRequest professorRegisterRequest) {
        String encodedPw = passwordEncoder.encode(professorRegisterRequest.getPw());
        professorRegisterRequest.setPw(encodedPw);

        ProfessorRegisterDTO professorRegisterDTO = modelMapper.map(professorRegisterRequest, ProfessorRegisterDTO.class);
        professorService.register(professorRegisterDTO);
    }

    @PostMapping("/inactive")
    @Valid
    void inactiveSubject(@Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGINP_PROFESSOR) Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(NoExistEntityException::new);
        professorService.inactive(professor);
    }
}
