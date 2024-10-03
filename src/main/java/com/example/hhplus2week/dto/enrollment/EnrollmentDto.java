package com.example.hhplus2week.dto.enrollment;

import lombok.Getter;
import lombok.Setter;

public class EnrollmentDto {

    @Getter
    @Setter
    public static class EnrollmentResponseDto {

        Long userId;
        Long courseId;
        Long capacity;
        Long enrollmentCount;


        public EnrollmentResponseDto(Long userId, Long courseId, Long capacity, Long enrollmentCount) {
            this.userId = userId;
            this.courseId = courseId;
            this.capacity = capacity;
            this.enrollmentCount = enrollmentCount;
        }

    }

    @Getter
    @Setter
    public static class EnrollmentRequestDto {
        Long userId;
        Long courseId;
    }
}
