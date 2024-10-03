package com.example.hhplus2week.service.course;

import com.example.hhplus2week.dto.course.CourseDto;

public interface CourseService {
    CourseDto.CourseResponseDto registerCourse(CourseDto.CourseRequestDto requestDto);
}
