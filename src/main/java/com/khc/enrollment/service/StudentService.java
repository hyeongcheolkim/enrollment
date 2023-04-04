package com.khc.enrollment.service;

import com.khc.enrollment.entity.MajorType;
import com.khc.enrollment.entity.member.MemberInfo;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.repository.StudentRepository;
import com.khc.enrollment.service.dto.StudentRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Student> login(String loginId, String pw) {
        return studentRepository.findAllByLoginId(loginId).stream()
                .filter(e -> e.getMemberInfo().getActivated())
                .filter(e -> passwordEncoder.matches(pw, e.getMemberInfo().getPw()))
                .findAny();
    }

    public Student register(StudentRegisterDTO studentRegisterDTO) {
        if(isDuplicatedLoginId(studentRegisterDTO.getLoginId()))
            throw new DuplicatedEntityException();

        Student student = Student.builder()
                .memberInfo(
                        MemberInfo.builder()
                        .loginId(studentRegisterDTO.getLoginId())
                        .pw(studentRegisterDTO.getPw())
                        .name(studentRegisterDTO.getName())
                        .activated(true)
                        .build())
                .majors(new HashMap<>(Map.of(MajorType.ORIGIN, studentRegisterDTO.getOriginDepartment())))
                .build();
        return studentRepository.save(student);
    }

    private boolean isDuplicatedLoginId(String loginId) {
        return studentRepository.findAllByLoginId(loginId).stream()
                .anyMatch(e -> e.getMemberInfo().getActivated());
    }

    public void inactive(Student student) {
        student.getMemberInfo().setActivated(false);
    }
}
