package com.khc.enrollment.service;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Course.Day;
import com.khc.enrollment.entity.Department;
import com.khc.enrollment.entity.Enrollment;
import com.khc.enrollment.entity.MajorType;
import com.khc.enrollment.entity.ScoreType;
import com.khc.enrollment.entity.member.Student;
import com.khc.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public void enroll(Student student, Course course) {
        if (isFullCapacity(course))
            throw new RuntimeException("코스 인원이 꽉 찼습니다. 수강신청할 수 없습니다");
        if (isNotQualified(student, course))
            throw new RuntimeException("수강 신청 자격이 없습니다");
        if (isDuplicatedTime(student, course))
            throw new RuntimeException("신청한 과목의 수업시간이 기존 시간표와 중복됩니다");

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        enrollmentRepository.save(enrollment);
    }

    public void drop(Enrollment enrollment) {
        enrollmentRepository.delete(enrollment);
    }

    public void grade(Enrollment enrollment, ScoreType scoreType) {
        enrollment.setOnSemester(false);
        enrollment.setScore(scoreType);
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
            return true;
        }
        return false;
    }
}
