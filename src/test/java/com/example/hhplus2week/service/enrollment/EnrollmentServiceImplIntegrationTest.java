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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class EnrollmentServiceImplIntegrationTest {

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
    void setup() {
        course = Course.builder()
                .name("Spring Framework")
                .capacity(30L)
                .build();
        courseRepository.save(course);

        for (long i = 1; i <= 40; i++) {
            User user = new User(i, "User " + i);
            userRepository.save(user);
        }
    }

    @Test
    @DisplayName("특강신청 동시성 통합 테스트")
    void raceConditionTest() throws InterruptedException {
        int threadCount = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // 성공 및 실패한 신청의 수를 추적하기 위한 리스트
        List<String> successMessages = Collections.synchronizedList(new ArrayList<>());
        List<String> failureMessages = Collections.synchronizedList(new ArrayList<>());

        for (long i = 1; i <= threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    EnrollmentDto.EnrollmentRequestDto requestDto = new EnrollmentDto.EnrollmentRequestDto();
                    requestDto.setUserId(userId);
                    requestDto.setCourseId(course.getId());
                    EnrollmentDto.EnrollmentResponseDto response = enrollmentService.registerEnrollment(requestDto);
                } catch (Exception e) {
                    failureMessages.add("Error -- " + userId + " == " + e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        // 모든 스레드가 종료될 때까지 대기
        countDownLatch.await();
        executorService.shutdown();

        // DB에서 강의 엔티티를 다시 가져옴
        Course updatedCourse = courseRepository.findById(course.getId()).orElseThrow();

        // 성공한 수강 신청의 개수 검증
        Assertions.assertEquals(30, updatedCourse.getEnrollCount(), "수강 신청 인원은 최대 30명이어야 합니다.");
        Assertions.assertEquals(30, successMessages.size(), "최대 30명까지만 수강 신청이 성공해야 합니다.");

        // 실패한 수강 신청의 개수를 출력
        failureMessages.forEach(System.out::println);
    }
}

