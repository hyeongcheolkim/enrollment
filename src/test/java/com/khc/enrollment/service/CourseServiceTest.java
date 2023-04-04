package com.khc.enrollment.service;

import com.khc.enrollment.entity.Classroom;
import com.khc.enrollment.entity.Course.CourseTime;
import com.khc.enrollment.entity.Course.Day;
import com.khc.enrollment.repository.ClassroomRepository;
import com.khc.enrollment.service.dto.CourseOpenDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;
    @Autowired
    private ClassroomRepository classroomRepository;

    @Test
    void 코스_시간표는_수업교실과_시간이_중복되게_만들_수_없다() {
        final Classroom classroom1 = Classroom.builder()
                .name("정보기술관")
                .code(200)
                .build();
        final Classroom classroom2 = Classroom.builder()
                .name("미래관")
                .code(300)
                .build();
        classroomRepository.saveAll(List.of(classroom1, classroom2));

        final CourseOpenDTO courseOpenDTO1 = CourseOpenDTO.builder()
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(9)
                        .endHour(13)
                        .build()))
                .classroom(classroom1)
                .build();
        final CourseOpenDTO courseOpenDTO2 = CourseOpenDTO.builder()
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(8)
                        .endHour(15)
                        .build()))
                .classroom(classroom1)
                .build();
        final CourseOpenDTO courseOpenDTO3 = CourseOpenDTO.builder()
                .courseTimes(List.of(CourseTime.builder()
                        .day(Day.MON)
                        .startHour(8)
                        .endHour(12)
                        .build()))
                .classroom(classroom2)
                .build();
        courseService.open(courseOpenDTO1);

        Assertions.assertThatThrownBy(() -> courseService.open(courseOpenDTO2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("선택한 교실과 시간이 다른 코스와 겹칩니다.");
        courseService.open(courseOpenDTO3);
    }
}