package com.khc.enrollment.controller;


import com.khc.enrollment.entity.Classroom;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.ClassroomRepository;
import com.khc.enrollment.service.ClassroomService;
import com.khc.enrollment.service.dto.CreateClassroomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
@Transactional
public class ClassroomController {

    private final ClassroomService classroomService;

    private final ClassroomRepository classroomRepository;

    @PostMapping("/create")
    @Valid
    public void createClassroom(
            @RequestParam @NotBlank String name,
            @RequestParam @NotNull Integer code){
        CreateClassroomDTO createClassroomDTO = CreateClassroomDTO.builder()
                .name(name)
                .code(code)
                .build();

        classroomService.create(createClassroomDTO);
    }

    @PostMapping("/inactivate")
    @Valid
    public void deleteClassroom(@RequestParam @NotNull Long classroomId){
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(NoExistEntityException::new);

        classroomService.inactivate(classroom);
    }
}
