package com.khc.enrollment.service;

import com.khc.enrollment.entity.*;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Course.Day;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.NotAuthorizedException;
import com.khc.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public Enrollment enroll(Student student, Course course) {

        if (isFullCapacity(course))
            throw new RuntimeException("코스 인원이 꽉 찼습니다. 수강신청할 수 없습니다");
        if (isDuplicatedTime(student, course))
            throw new RuntimeException("신청한 과목의 수업시간이 기존 시간표와 중복됩니다");
        if (isDuplicatedEnroll(student, course))
            throw new RuntimeException("이미 수강한 과목입니다, 재수강은 B0 미만만 가능합니다");
        if(isNotQualified(student, course))
            throw new RuntimeException("수강 금지과입니다");

        List<Enrollment> enrollments = enrollmentRepository.findAllByStudent(student);
        if (isDuplicatedOnSemesterEnroll(course, enrollments))
            throw new RuntimeException("이미 신청한 과목입니다");

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        return enrollmentRepository.save(enrollment);
    }

    private boolean isNotQualified(Student student, Course course) {
        return course.getProhibitedDepartments().stream()
                .map(Department::getCode)
                .anyMatch(e -> e.equals(student.getDepartment().getCode()));
    }

    public Map<Basket, Boolean> enrollBaskets(Student student, List<Basket> baskets){
        var ret = new HashMap<Basket, Boolean>();

        for(var basket : baskets){
            try{
                Enrollment enrollment = this.enroll(student, basket.getCourse());
                ret.put(basket, true);
            }catch(Exception e){
                ret.put(basket, false);
            }
        }

        return ret;
    }

    public void drop(Student student, Enrollment enrollment) {
        if (!student.equals(enrollment.getStudent()))
            throw new NotAuthorizedException();

        enrollmentRepository.delete(enrollment);
        enrollmentRepository.flush();
    }

    public void grade(Professor professor, Enrollment enrollment, ScoreType scoreType) {
        if (!professor.equals(enrollment.getCourse().getProfessor()))
            throw new NotAuthorizedException();

        removePastEnrollmentIfPresent(enrollment);

        enrollment.setOnSemester(false);
        enrollment.setScoreType(scoreType);
    }

    private void removePastEnrollmentIfPresent(Enrollment enrollment) {
        Integer code = enrollment.getCourse().getSubject().getCode();
        enrollmentRepository.findAllByStudent(enrollment.getStudent()).stream()
                .filter(e -> !e.isOnSemester())
                .filter(e -> e.getCourse().getSubject().getCode().equals(code))
                .findAny()
                .ifPresent(enrollmentRepository::delete);
    }

    private boolean isDuplicatedTime(Student student, Course course) {
        List<Enrollment> enrollments = enrollmentRepository.findAllByStudent(student);

        Map<Day, List<CourseTime>> courseTimeGroupByDay = enrollments.stream()
                .filter(Enrollment::isOnSemester)
                .map(Enrollment::getCourse)
                .map(Course::getCourseTime)
                .collect(Collectors.groupingBy(CourseTime::getDay));

            Integer startHour = course.getCourseTime().getStartHour();
            Integer endHour = course.getCourseTime().getEndHour();
            Day day = course.getCourseTime().getDay();

            return courseTimeGroupByDay.getOrDefault(day, List.of()).stream()
                    .anyMatch(e -> startHour < e.getStartHour() && e.getStartHour() < endHour
                            && startHour < e.getEndHour() && e.getEndHour() < endHour);
    }

    private boolean isFullCapacity(Course course) {
        Long cnt = enrollmentRepository.countAllByCourse(course);
        return cnt >= course.getCapacity();
    }


    private boolean isDuplicatedOnSemesterEnroll(Course course, List<Enrollment> enrollments) {
        return enrollments.stream()
                .filter(Enrollment::isOnSemester)
                .map(e -> e.getCourse().getSubject().getCode())
                .anyMatch(e -> e.equals(course.getSubject().getCode()));
    }

    private boolean isDuplicatedEnroll(Student student, Course course) {
        return student.getEnrollments().stream()
                .filter(e -> e.getCourse().getSubject().getCode().equals(course.getSubject().getCode()))
                .filter(e -> !e.isOnSemester())
                .anyMatch(e -> e.getScoreType().getDigit() >= ScoreType.B_ZERO.getDigit());
    }
}
