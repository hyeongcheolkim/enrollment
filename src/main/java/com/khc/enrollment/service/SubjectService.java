package com.khc.enrollment.service;

import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.repository.SubjectRepository;
import com.khc.enrollment.service.dto.SubjectMakeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    public void make(SubjectMakeDTO subjectMakeDTO){
        if(isDuplicatedSubject(subjectMakeDTO.getCode()))
            throw new DuplicatedEntityException("과목코드가 중복됩니다");

        Subject subject = modelMapper.map(subjectMakeDTO, Subject.class);
        subjectRepository.save(subject);
    }

    public void inactive(Subject subject){
        subject.setActivated(false);
    }

    private boolean isDuplicatedSubject(Integer code) {
        return subjectRepository.findAllByCode(code).stream()
                .filter(Subject::getActivated)
                .anyMatch(e -> e.getCode().equals(code));
    }
}