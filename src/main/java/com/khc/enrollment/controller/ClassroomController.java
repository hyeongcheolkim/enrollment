package com.khc.enrollment.controller;


import com.khc.enrollment.aop.annotation.PermitAdmin;
import com.khc.enrollment.aop.annotation.PermitAnyLogin;
import com.khc.enrollment.aop.annotation.PermitProfessor;
import com.khc.enrollment.controller.response.ClassroomListResponse;
import com.khc.enrollment.controller.response.ClassroomResponse;
import com.khc.enrollment.controller.response.SubjectResponse;
import com.khc.enrollment.entity.Classroom;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.ClassroomRepository;
import com.khc.enrollment.service.ClassroomService;
import com.khc.enrollment.service.dto.CreateClassroomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
@Transactional
public class ClassroomController {

    private final ClassroomService classroomService;

    private final ClassroomRepository classroomRepository;

    @PermitAdmin
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

    @PermitAnyLogin
    @PostMapping("/inactivate")
    @Valid
    public void deleteClassroom(@RequestParam @NotNull Long classroomId){
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(NoExistEntityException::new);

        classroomService.inactivate(classroom);
    }

    @PostMapping("/list")
    ResponseEntity<ClassroomListResponse> classroomList() {
        final List<Classroom> classrooms = classroomRepository.findAllByActivatedTrue().stream()
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ClassroomListResponse(classrooms));
    }
}
