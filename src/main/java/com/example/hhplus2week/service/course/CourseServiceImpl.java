package com.example.hhplus2week.service.course;

import com.example.hhplus2week.domain.Course;
import com.example.hhplus2week.dto.course.CourseDto;
import com.example.hhplus2week.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;


    /**
     * 특강 수업 등록
     */
    @Transactional
    public CourseDto.CourseResponseDto registerCourse(CourseDto.CourseRequestDto requestDto) {
        Course course = courseRepository.findByName(requestDto.getName());
        if(course != null) {
            throw new IllegalStateException("이미 강의 존재");
        }

        Course newCourse = Course.builder()
                .id(requestDto.getCourseId())
                .name(requestDto.getName())
                .capacity(30L)
                .build();

        courseRepository.save(newCourse);

        return new CourseDto.CourseResponseDto(
                newCourse.getId(),
                newCourse.getName(),
                newCourse.getCapacity(),
                newCourse.getEnrollCount()
        );
    }

}
