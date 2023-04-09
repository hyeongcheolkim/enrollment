package com.khc.enrollment.controller.response;

import com.khc.enrollment.entity.Basket;
import lombok.Data;

@Data
public class BasketResponse {
    private Long basketId;

    private Long studentId;

    private String studentName;

    private Long courseId;

    private Integer capacity;

    private Long subjectId;

    private String subjectName;

    private Integer division;

    private Integer subjectCode;

    public BasketResponse(Basket basket){
        this.studentId = basket.getStudent().getId();
        this.studentName = basket.getStudent().getMemberInfo().getName();
        this.courseId = basket.getCourse().getId();
        this.capacity = basket.getCourse().getCapacity();
        this.subjectId = basket.getCourse().getSubject().getId();
        this.subjectName = basket.getCourse().getSubject().getName();
        this.division = basket.getCourse().getDivision();
        this.basketId = basket.getId();
        this.subjectCode = basket.getCourse().getSubject().getCode();
    }
}
