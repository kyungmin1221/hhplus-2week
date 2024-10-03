package com.example.hhplus2week.dto.user;

import lombok.Getter;
import lombok.Setter;

public class UserDto {

    @Getter
    @Setter
    public static class UserResponseDto {

        Long userId;
        String name;

        public UserResponseDto(Long userId, String name) {
            this.userId = userId;
            this.name = name;
        }
    }

    @Getter
    @Setter
    public static class UserRequestDto {
        Long userId;
        String name;
    }
}
