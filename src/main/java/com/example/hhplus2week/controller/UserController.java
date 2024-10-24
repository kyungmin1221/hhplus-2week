package com.example.hhplus2week.controller;

import com.example.hhplus2week.dto.course.CourseDto;
import com.example.hhplus2week.dto.user.UserDto;
import com.example.hhplus2week.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto.UserResponseDto> registerUser(
            @RequestBody UserDto.UserRequestDto requestDto) {
        UserDto.UserResponseDto responseDto = userService.registerUser(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponseDto> getUserInfo(@PathVariable Long userId) {
        UserDto.UserResponseDto userResponseDto = userService.getUserInfo(userId);
        return ResponseEntity.ok(userResponseDto);
    }

}
