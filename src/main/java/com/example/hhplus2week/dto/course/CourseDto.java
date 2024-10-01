package com.example.hhplus2week.dto.course;

import lombok.Getter;
import lombok.Setter;

public class CourseDto {


    @Getter
    @Setter
    public static class CourseResponseDto {

        Long courseId;
        String name;
        Long capacity;

        public CourseResponseDto(Long courseId, String name, Long capacity) {
            this.courseId = courseId;
            this.name = name;
            this.capacity = capacity;
        }
    }

    @Getter
    @Setter
    public static class CourseRequestDto {
        Long courseId;
        String name;
        Long capacity;
    }
}
