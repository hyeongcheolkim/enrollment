package com.khc.enrollment.service;

import com.khc.enrollment.entity.Basket;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.DuplicatedEntityException;
import com.khc.enrollment.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasketService {

    private final BasketRepository basketRepository;
    private final EnrollmentService enrollmentService;

    public Basket put(Student student, Course course) {
        if (isDuplicatedBasket(student, course))
            throw new DuplicatedEntityException();

        Basket basket = Basket.builder()
                .student(student)
                .course(course)
                .build();

        return basketRepository.save(basket);
    }

    private boolean isDuplicatedBasket(Student student, Course course) {
        return student.getBaskets().stream()
                .map(Basket::getCourse)
                .anyMatch(e -> e.getSubject().getCode().equals(course.getSubject().getCode())
                        && e.getProfessor().getId().equals(course.getProfessor().getId())
                        && e.getDivision().equals(course.getDivision()));
    }

    public void erase(Basket basket) {
        basketRepository.delete(basket);
        basketRepository.flush();
    }
}
