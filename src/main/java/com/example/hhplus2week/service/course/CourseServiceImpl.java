package com.example.hhplus2week.service.course;

import com.example.hhplus2week.domain.Course;
import com.example.hhplus2week.dto.course.CourseDto;
import com.example.hhplus2week.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                .createdAt(LocalDateTime.now())
                .build();

        courseRepository.save(newCourse);

        return new CourseDto.CourseResponseDto(newCourse);
    }

    /**
     * 특강 목록 조회
     */
    public List<CourseDto.CourseResponseDto> getListCourse() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }


    // course -> dto 변환
    private CourseDto.CourseResponseDto convertToDto(Course course) {
        CourseDto.CourseResponseDto responseDto = new CourseDto.CourseResponseDto();
        responseDto.setCourseId(course.getId());
        responseDto.setName(course.getName());
        responseDto.setCapacity(course.getCapacity());
        responseDto.setEnrollCount(course.getEnrollCount());
        responseDto.setCreatedAt(LocalDateTime.now());

        return responseDto;
    }

}
