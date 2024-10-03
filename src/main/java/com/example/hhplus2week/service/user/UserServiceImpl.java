package com.example.hhplus2week.service.user;

import com.example.hhplus2week.domain.User;
import com.example.hhplus2week.dto.user.UserDto;
import com.example.hhplus2week.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    /**
     * 유저를 등록
     */
    @Transactional
    public UserDto.UserResponseDto registerUser(UserDto.UserRequestDto requestDto) {
        Optional<User> user = userRepository.findById(requestDto.getUserId());
        if(user.isPresent()) {
            throw new IllegalStateException("이미 유저 있음");
        }

        User newUser = User.builder()
                .name(requestDto.getName())
                .build();

        User savedUser = userRepository.save(newUser);

        return new UserDto.UserResponseDto(
                savedUser.getId(),
                savedUser.getName());
    }

    /**
     * 유저를 조회
     */
    public UserDto.UserResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return new UserDto.UserResponseDto(user.getId(), user.getName());
    }

}
