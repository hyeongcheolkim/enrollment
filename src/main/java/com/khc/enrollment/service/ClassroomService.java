package com.khc.enrollment.service;

import com.khc.enrollment.entity.Classroom;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.repository.ClassroomRepository;
import com.khc.enrollment.service.dto.CreateClassroomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final ModelMapper modelMapper;

    public Classroom create(CreateClassroomDTO createClassroomDTO){
        if(isDuplicatedClassroom(createClassroomDTO.getCode()))
            throw new DuplicatedEntityException("교실 코드가 중복됩니다");

        Classroom classroom = modelMapper.map(createClassroomDTO, Classroom.class);
        return classroomRepository.save(classroom);
    }

    private boolean isDuplicatedClassroom(Integer code) {
        return classroomRepository.findAllByCode(code).stream()
                .anyMatch(e -> e.getCode().equals(code));
    }

    public void inactivate(Classroom classroom){
        classroom.setActivated(false);
    }
}
