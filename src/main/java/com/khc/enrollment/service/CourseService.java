package com.khc.enrollment.service;

import com.khc.enrollment.entity.Course.Course;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Course.Day;
import com.khc.enrollment.repository.CourseRepository;
import com.khc.enrollment.service.dto.CourseOpenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public void open(CourseOpenDTO courseOpenDTO) {
        if (isDuplicatedCourseTime(courseOpenDTO.getCourseTimes()))
            throw new RuntimeException("선택한 코스시간표가 이미 존재하는 다른 코스시간표와 겹칩니다.");

        Course course = modelMapper.map(courseOpenDTO, Course.class);
        courseRepository.save(course);
    }

    public void close(Course course){
        courseRepository.delete(course);
    }

    private boolean isDuplicatedCourseTime(List<CourseTime> courseTimes) {
        for (final var courseTime : courseTimes) {
            final Day day = courseTime.getDay();
            final Integer startHour = courseTime.getStartHour();
            final Integer endHour = courseTime.getEndHour();

            List<Course> allCourseByDay = courseRepository.findAllCourseByDay(day);
            boolean duplicated = allCourseByDay.stream()
                    .map(Course::getCourseTimes)
                    .flatMap(List::stream)
                    .anyMatch(e -> startHour < e.getStartHour() && e.getStartHour() < endHour
                            && startHour < e.getEndHour() && e.getEndHour() < endHour);
            if(duplicated)
                return true;
        }
        return false;
    }
}