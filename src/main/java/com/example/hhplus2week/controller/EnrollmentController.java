package com.example.hhplus2week.controller;

import com.example.hhplus2week.dto.enrollment.EnrollmentDto;
import com.example.hhplus2week.service.enrollment.EnrollmentService;
import com.example.hhplus2week.service.enrollment.EnrollmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentServiceImpl enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentDto.EnrollmentResponseDto> enrollInCourse(
            @RequestBody EnrollmentDto.EnrollmentRequestDto requestDto) {

        EnrollmentDto.EnrollmentResponseDto responseDto = enrollmentService.registerEnrollment(requestDto);
        if ("수강 신청 완료".equals(responseDto.getMessage())) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}
