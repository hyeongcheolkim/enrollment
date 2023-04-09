package com.khc.enrollment.controller;

import com.khc.enrollment.aop.annotation.PermitAdmin;
import com.khc.enrollment.aop.annotation.PermitAnyLogin;
import com.khc.enrollment.controller.request.SubjectMakeRequest;
import com.khc.enrollment.controller.response.SubjectListResponse;
import com.khc.enrollment.controller.response.SubjectResponse;
import com.khc.enrollment.entity.Subject;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.SubjectRepository;
import com.khc.enrollment.service.SubjectService;
import com.khc.enrollment.service.dto.SubjectMakeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
@Transactional
public class SubjectRestController {

    private final SubjectService subjectService;

    private final SubjectRepository subjectRepository;

    @PermitAdmin
    @PostMapping("/make")
    void makeSubject(@RequestBody @Validated SubjectMakeRequest subjectMakeRequest) {

        SubjectMakeDTO subjectMakeDTO = SubjectMakeDTO.builder()
                .code(subjectMakeRequest.getCode())
                .name(subjectMakeRequest.getName())
                .credit(subjectMakeRequest.getCredit())
                .type(subjectMakeRequest.getType())
                .build();

        subjectService.make(subjectMakeDTO);
    }

    @PermitAdmin
    @PostMapping("/inactive")
    @Valid
    void inactiveSubject(@RequestParam @NotNull Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(NoExistEntityException::new);

        subjectService.inactive(subject);
    }

    @PostMapping("/list")
    ResponseEntity<SubjectListResponse> subjectList() {
        final List<Subject> subjects = subjectRepository.findAllByActivatedTrue();

        return ResponseEntity.ok(new SubjectListResponse(subjects));
    }
}
