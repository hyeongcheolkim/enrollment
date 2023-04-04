package com.khc.enrollment.service;

import com.khc.enrollment.entity.Classroom;
import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Course.Day;
import com.khc.enrollment.entity.member.Professor;
import com.khc.enrollment.exception.exceptoin.NotAuthenticatedException;
import com.khc.enrollment.repository.CourseRepository;
import com.khc.enrollment.service.dto.CourseOpenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public Course open(CourseOpenDTO courseOpenDTO) {
        if (isDuplicatedCourseTimeAndClassroom(courseOpenDTO.getCourseTimes(), courseOpenDTO.getClassroom()))
            throw new RuntimeException("선택한 교실과 시간이 다른 코스와 겹칩니다.");

        Course course = modelMapper.map(courseOpenDTO, Course.class);

        return courseRepository.save(course);
    }

    public void close(Course course, Professor professor) {
        if (!course.getProfessor().equals(professor))
            throw new NotAuthenticatedException();

        courseRepository.delete(course);
    }

    private boolean isDuplicatedCourseTimeAndClassroom(List<CourseTime> courseTimes, Classroom classroom) {

        for (final var courseTime : courseTimes) {
            final Day day = courseTime.getDay();
            final Integer startHour = courseTime.getStartHour();
            final Integer endHour = courseTime.getEndHour();

            List<Course> allCourseByDay = courseRepository.findAllCourseByDay(day);
            return allCourseByDay.stream()
                    .filter(e -> e.getClassroom().equals(classroom))
                    .map(Course::getCourseTimes)
                    .flatMap(List::stream)
                    .anyMatch(e -> e.getStartHour() <= startHour && startHour < e.getEndHour()
                            || e.getStartHour() < endHour && endHour <= e.getEndHour()
                            || startHour <= e.getStartHour() && e.getStartHour() < endHour
                            || startHour < e.getEndHour() && e.getEndHour() <= endHour);
        }
        return false;
    }
}
