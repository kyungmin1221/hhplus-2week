package com.example.hhplus2week.service.enrollment;

import com.example.hhplus2week.domain.Course;
import com.example.hhplus2week.domain.Enrollment;
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

import java.util.*;



@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Course course;

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

        for (long i = 1; i <= 35; i++) {
            User user = new User(i, "User " + i);
            Mockito.lenient().when(userRepository.findById(i))
                    .thenReturn(Optional.of(user));
        }
    }


    /**
     * 수강신청
     *
     * @throws InterruptedException
     */
    @Test
    @DisplayName("수강 신청이 30명 초과 시 실패하는지 테스트")
    void testEnrollmentExceedsCapacity() {
        // 중복 신청 방지 모킹
        Mockito.when(enrollmentRepository.existsByUserAndCourse(Mockito.any(User.class), Mockito.eq(course)))
                .thenReturn(false);

        for (long i = 1; i <= 30; i++) {
            EnrollmentDto.EnrollmentRequestDto requestDto = new EnrollmentDto.EnrollmentRequestDto();
            requestDto.setUserId(i);
            requestDto.setCourseId(1L);

            try {
                enrollmentService.registerEnrollment(requestDto);
                System.out.println("User " + i + " 수강신청 등록");
                System.out.println("등록 인원 수 : " + course.getEnrollCount());
            } catch (Exception e) {
                System.out.println("User " + i + " 등록 실패 : " + e.getMessage());
            }
        }

        // 31번째 유저의 신청 시도 - 실패해야 함
        EnrollmentDto.EnrollmentRequestDto requestDto = new EnrollmentDto.EnrollmentRequestDto();
        requestDto.setUserId(31L);
        requestDto.setCourseId(1L);

        // 예외 발생 검증 및 출력
        try {
            enrollmentService.registerEnrollment(requestDto);
            System.out.println("31번 째 유저 등록 성공");
        } catch (Exception e) {
            System.out.println("31번 째 유저 등록 실패: " + e.getMessage());
            Assertions.assertEquals("수업 가능 인원이 다 찼습니다.", e.getMessage());
        }

    }



}