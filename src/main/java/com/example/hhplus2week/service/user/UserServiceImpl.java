package com.example.hhplus2week.service.user;

import com.example.hhplus2week.domain.User;
import com.example.hhplus2week.dto.user.UserDto;
import com.example.hhplus2week.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    /**
     * 유저를 조회
     */
    public UserDto.UserResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return new UserDto.UserResponseDto(user.getId(), user.getName());
    }


    /**
     * 유저 중복 검증
     */
}
