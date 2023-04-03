package com.khc.enrollment.service;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Course.Day;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.Enrollment;
import com.khc.enrollment.entity.MajorType;
import com.khc.enrollment.entity.ScoreType;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.exception.exceptoin.NotAuthenticatedException;
import com.khc.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public void enroll(Student student, Course course) {

        if (isFullCapacity(course))
            throw new RuntimeException("코스 인원이 꽉 찼습니다. 수강신청할 수 없습니다");
        if (isNotQualified(student, course))
            throw new RuntimeException("수강 신청 자격이 없습니다");
        if (isDuplicatedTime(student, course))
            throw new RuntimeException("신청한 과목의 수업시간이 기존 시간표와 중복됩니다");
        if (isDuplicatedEnroll(student, course))
            throw new RuntimeException("이미 수강한 과목입니다, 재수강은 B0 미만만 가능합니다");

        List<Enrollment> enrollments = enrollmentRepository.findAllByStudent(student);
        if (isDuplicatedOnSemesterEnroll(course, enrollments))
            throw new RuntimeException("이미 신청한 과목입니다");

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        enrollmentRepository.save(enrollment);
    }

    public void drop(Student student, Enrollment enrollment) {
        if (!student.equals(enrollment.getStudent()))
            throw new NotAuthenticatedException();

        enrollmentRepository.delete(enrollment);
    }

    public void grade(Professor professor, Enrollment enrollment, ScoreType scoreType) {
        if (!professor.equals(enrollment.getCourse().getProfessor()))
            throw new NotAuthenticatedException();

        removePastEnrollmentIfPresent(enrollment);

        enrollment.setOnSemester(false);
        enrollment.setScore(scoreType);
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
                .map(Enrollment::getCourse)
                .map(Course::getCourseTimes)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(CourseTime::getDay));

        for (final var courseTime : course.getCourseTimes()) {
            Integer startHour = courseTime.getStartHour();
            Integer endHour = courseTime.getEndHour();
            Day day = courseTime.getDay();

            return courseTimeGroupByDay.getOrDefault(day, List.of()).stream()
                    .anyMatch(e -> startHour < e.getStartHour() && e.getStartHour() < endHour
                            && startHour < e.getEndHour() && e.getEndHour() < endHour);
        }
        return false;
    }

    private boolean isFullCapacity(Course course) {
        Long cnt = enrollmentRepository.countAllByCourse(course);
        return cnt >= course.getCapacity();
    }

    private boolean isNotQualified(Student student, Course course) {
        Map<MajorType, Department> majors = student.getMajors();
        List<Department> allowedDepartments = course.getAllowedDepartments();
        List<MajorType> prohibitedMajorTypes = course.getProhibitedMajorTypes();

        for (var majorType : MajorType.values()) {
            if (prohibitedMajorTypes.contains(majorType))
                continue;
            if (!majors.containsKey(majorType))
                continue;
            if (!allowedDepartments.contains(majors.get(majorType)))
                continue;
            return false;
        }
        return true;
    }

    private boolean isDuplicatedOnSemesterEnroll(Course course, List<Enrollment> enrollments) {
        return enrollments.stream()
                .filter(Enrollment::isOnSemester)
                .map(Enrollment::getCourse)
                .anyMatch(e -> e.equals(course));
    }

    private boolean isDuplicatedEnroll(Student student, Course course) {
        return student.getEnrollments().stream()
                .filter(e -> !e.isOnSemester())
                .filter(e -> e.getScore().getDigit() < ScoreType.B_ZERO.getDigit())
                .map(e -> e.getCourse().getSubject().getCode())
                .anyMatch(e -> e.equals(course.getSubject().getCode()));
    }
}
