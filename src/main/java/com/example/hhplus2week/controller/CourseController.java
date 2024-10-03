package com.example.hhplus2week.controller;

import com.example.hhplus2week.dto.course.CourseDto;
import com.example.hhplus2week.service.course.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @PostMapping("/register")
    public ResponseEntity<CourseDto.CourseResponseDto> registerCourse(
            @RequestBody CourseDto.CourseRequestDto requestDto) {
        CourseDto.CourseResponseDto responseDto = courseService.registerCourse(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CourseDto.CourseResponseDto>>getListCourse()  {
        List<CourseDto.CourseResponseDto> responseDtos = courseService.getListCourse();
        return ResponseEntity.ok(responseDtos);

    }


}
