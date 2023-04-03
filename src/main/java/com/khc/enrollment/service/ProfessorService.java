package com.khc.enrollment.service;

import com.khc.enrollment.entity.member.MemberInfo;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.repository.ProfessorRepository;
import com.khc.enrollment.service.dto.ProfessorRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Professor> login(String loginId, String pw) {
        return professorRepository.findAllByLoginId(loginId).stream()
                .filter(e -> e.getMemberInfo().getActivated())
                .filter(e -> passwordEncoder.matches(pw, e.getMemberInfo().getPw()))
                .findAny();
    }

    public void register(ProfessorRegisterDTO professorRegisterDTO) {
        if(isDuplicatedLoginId(professorRegisterDTO.getLoginId()))
            throw new DuplicatedEntityException();

        Professor professor = Professor.builder()
                .memberInfo(
                        MemberInfo.builder()
                                .loginId(professorRegisterDTO.getLoginId())
                                .pw(professorRegisterDTO.getPw())
                                .name(professorRegisterDTO.getName())
                                .activated(true)
                                .build())
                .build();

        professorRepository.save(professor);
    }

    private boolean isDuplicatedLoginId(String loginId) {
        return professorRepository.findAllByLoginId(loginId).stream()
                .anyMatch(e -> e.getMemberInfo().getActivated());
    }

    public void inactive(Professor professor){
        professor.getMemberInfo().setActivated(false);
    }
}
