package com.example.hhplus2week.service.user;


import com.example.hhplus2week.dto.user.UserDto;

public interface UserService {

    /**
     * 특정 유저 조회
     * @param userId
     * @return
     */
    UserDto.UserResponseDto getUserInfo(Long userId);
}
