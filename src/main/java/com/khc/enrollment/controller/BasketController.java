package com.khc.enrollment.controller;

import com.khc.enrollment.aop.annotation.PermitStudent;
import com.khc.enrollment.entity.Basket;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.NoExistEntityException;
import com.khc.enrollment.repository.BasketRepository;
import com.khc.enrollment.repository.CourseRepository;
import com.khc.enrollment.repository.StudentRepository;
import com.khc.enrollment.service.BasketService;
import com.khc.enrollment.session.SessionConst;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
@Transactional
public class BasketController {

    private final BasketService basketService;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final BasketRepository basketRepository;

    @PermitStudent
    @PostMapping("/put")
    void putBasket(
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_STUDENT, required = false) Long studentId,
            @RequestParam Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(NoExistEntityException::new);
        Student student = studentRepository.findById(studentId).orElseThrow(NoExistEntityException::new);

        basketService.put(student, course);
    }

    @PermitStudent
    @PostMapping("/erase")
    void eraseBasket(@RequestParam Long basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow();

        basketService.erase(basket);
    }
}
