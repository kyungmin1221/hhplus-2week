package com.example.hhplus2week.service.enrollment;

import com.example.hhplus2week.dto.enrollment.EnrollmentDto;

public interface EnrollmentService {

    EnrollmentDto.EnrollmentResponseDto registerEnrollment(EnrollmentDto.EnrollmentRequestDto requestDto);
}
