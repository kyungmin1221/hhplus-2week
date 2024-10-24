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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class EnrollmentIntegrationTest {

    @Autowired
    private EnrollmentServiceImpl enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private Course course;

    @BeforeEach
    @DisplayName("테스트 데이터 초기화")
    void init() {
        // Course 객체를 생성하고 데이터베이스에 저장
        course = Course.builder()
                .name("spring framework")
                .capacity(30L)
                .build();
        courseRepository.save(course);
        System.out.println("course = " + course.getId());

        // 40명의 User 객체를 생성하고 데이터베이스에 저장
        for (long i = 1; i <= 40; i++) {
            User user = new User(i, "User " + i);
            userRepository.save(user);
        }
    }

    @Test
    @DisplayName("최대 30명 - 40명 수강 신청 시 실패를 확인하는 테스트")
    void testConcurrencyTest() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        List<String> successMessages = Collections.synchronizedList(new ArrayList<>());
        List<String> failureMessages = Collections.synchronizedList(new ArrayList<>());

        for (long i = 1L; i <= threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    EnrollmentDto.EnrollmentRequestDto requestDto = new EnrollmentDto.EnrollmentRequestDto();
                    requestDto.setUserId(userId);
                    requestDto.setCourseId(course.getId());
                    System.out.println("requestDto.getUserId() = " + requestDto.getUserId());
                    System.out.println("requestDto.getCourseId() = " + requestDto.getCourseId());
                    enrollmentService.registerEnrollment(requestDto);
                    successMessages.add("User " + userId + " 수강신청 등록");
                } catch (Exception e) {
                    failureMessages.add("User " + userId + " 등록 실패 : " + e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // DB에서 강의 엔티티를 다시 가져옴
        Course updatedCourse = courseRepository.findById(course.getId()).orElseThrow();

        successMessages.forEach(System.out::println);
        failureMessages.forEach(System.out::println);

        // 성공한 수강 신청의 개수 검증
        Assertions.assertEquals(30, updatedCourse.getEnrollCount(),
                "수강 신청 인원은 최대 30명이어야 합니다.");
        Assertions.assertEquals(30, successMessages.size(),
                "최대 30명까지만 수강 신청이 성공해야 합니다.");
    }
}
