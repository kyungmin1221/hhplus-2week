package com.example.hhplus2week.service.enrollment;

import com.example.hhplus2week.domain.Course;
import com.example.hhplus2week.domain.User;
import com.example.hhplus2week.dto.enrollment.EnrollmentDto;
import com.example.hhplus2week.repository.CourseRepository;
import com.example.hhplus2week.repository.EnrollmentRepository;
import com.example.hhplus2week.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EnrollmentDuplicateTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Mock
    private Course course;

    @Mock
    private User user;

    @BeforeEach
    @DisplayName("테스트 데이터 초기화")
    void init() {
        course = Course.builder()
                .id(1L)
                .name("spring framework")
                .capacity(30L)
                .build();

        Mockito.when(courseRepository.findByIdWithLock(1L))
                .thenReturn(Optional.of(course));


        user = new User(1L, "User1");
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(enrollmentRepository.existsByUserAndCourse(user, course))
                .thenReturn(false)
                .thenReturn(true);
    }

    @Test
    @DisplayName("동일 유저 5번 신청 시 1번만 성공 하는지 테스트")
    void fiveTest() {
        int successCount = 0;
        int failCount = 0;
        EnrollmentDto.EnrollmentRequestDto requestDto = new EnrollmentDto.EnrollmentRequestDto();
        requestDto.setUserId(user.getId());
        requestDto.setCourseId(course.getId());

        for(int i = 0; i < 5; i++) {
            try {
                enrollmentService.registerEnrollment(requestDto);
                successCount ++ ;
            } catch (Exception e) {
                failCount ++;
                System.out.println(" ===== 실패 ======" +  e.getMessage());
            }
        }

        Assertions.assertEquals(1, successCount);
        Assertions.assertEquals(4, failCount);

    }
}
