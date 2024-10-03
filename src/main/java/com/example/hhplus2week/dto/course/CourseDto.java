package com.example.hhplus2week.dto.course;

import com.example.hhplus2week.domain.Course;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CourseDto {


    @Getter
    @Setter
    public static class CourseResponseDto {

        Long courseId;
        String name;
        Long capacity;
        Long enrollCount;
        LocalDateTime createdAt;

        public CourseResponseDto(Course course) {
            this.courseId = course.getId();
            this.name = course.getName();
            this.capacity = course.getCapacity();
            this.enrollCount = course.getEnrollCount();
            this.createdAt = course.getCreatedAt();
        }

        public CourseResponseDto() {

        }

    }

    @Getter
    @Setter
    public static class CourseRequestDto {
        Long courseId;
        String name;
    }
}
