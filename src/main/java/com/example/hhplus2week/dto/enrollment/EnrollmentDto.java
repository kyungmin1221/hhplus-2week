package com.example.hhplus2week.dto.enrollment;

import lombok.Getter;
import lombok.Setter;

public class EnrollmentDto {

    @Getter
    @Setter
    public static class EnrollmentResponseDto {

        Long userId;
        Long courseId;
        String message;


        public EnrollmentResponseDto(Long userId, Long courseId, String message) {
            this.userId = userId;
            this.courseId = courseId;
        }

        public EnrollmentResponseDto(String message) {
            this.message = message;
        }

    }

    @Getter
    @Setter
    public static class EnrollmentRequestDto {
        Long userId;
        Long courseId;
    }
}
