package com.example.hhplus2week.service.user;

import com.example.hhplus2week.domain.User;
import com.example.hhplus2week.dto.user.UserDto;
import com.example.hhplus2week.repository.CourseRepository;
import com.example.hhplus2week.repository.EnrollmentRepository;
import com.example.hhplus2week.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;


    /**
     * 유저 등록 테스트
     */
    @DisplayName("유저가 등록되었는지 확인하는 테스트")
    @Test
    public void registerUser() {

        long userId = 1L;
        String name = "java";
        // given
        UserDto.UserRequestDto requestDto = new UserDto.UserRequestDto();
        requestDto.setName("java");

        User rgUser = User.builder()
                .userId(userId)
                .name(name)
                .build();

        // when
        when(userRepository.save(any(User.class)))
                .thenReturn(rgUser);

        UserDto.UserResponseDto userResponseDto = userService.registerUser(requestDto);

        // then
        assertNotNull(userResponseDto);
        assertEquals(1L, userResponseDto.getUserId());
        assertEquals("java", userResponseDto.getName());

        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * 해당 유저가 존재하는지 확인
     */
    @DisplayName("유저가 존재하는지 확인하는 테스트")
    @Test
    public void getUserService() {
        long userId = 1L;
        String name = "java";
        User user = new User(userId, name);

        // given
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));


        // when
        UserDto.UserResponseDto userResponseDto = userService.getUserInfo(userId);

        // then
        assertEquals(userId, userResponseDto.getUserId());
        assertEquals(name, userResponseDto.getName());
    }


}