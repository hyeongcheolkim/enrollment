package com.khc.enrollment.controller;

import com.khc.enrollment.controller.request.SubjectMakeRequest;
import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.SubjectRepository;
import com.khc.enrollment.service.SubjectService;
import com.khc.enrollment.service.dto.SubjectMakeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectRestController {

    private final SubjectService subjectService;

    private final SubjectRepository subjectRepository;
    
    @PostMapping("/make")
    void makeSubject(@RequestBody @Valid SubjectMakeRequest subjectMakeRequest){
        final Subject prerequisite = subjectRepository.findById(subjectMakeRequest.getPrerequisiteId())
                .orElseThrow(NoExistEntityException::new);

        SubjectMakeDTO subjectMakeDTO = SubjectMakeDTO.builder()
                .prerequisite(prerequisite)
                .code(subjectMakeRequest.getCode())
                .name(subjectMakeRequest.getName())
                .credit(subjectMakeRequest.getCredit())
                .type(subjectMakeRequest.getType())
                .build();

        subjectService.make(subjectMakeDTO);
    }

    @PostMapping("/inactive")
    @Valid
    void inactiveSubject(@RequestParam("subjectId") @NotNull Long subjectId){
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(NoExistEntityException::new);

        subjectService.inactive(subject);
    }
}
