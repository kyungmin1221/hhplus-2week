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
public class EnrollmentServiceImplConcurrencyTest {

    @Autowired
    private EnrollmentServiceImpl enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;
    
    private Course course;

    @BeforeEach
    void setup() {
        // Course 객체 초기화
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
    @DisplayName("동시성 테스트 - 최대 30명만 수강 신청 성공")
    void testConcurrentEnrollment() throws InterruptedException {
        int threadCount = 40; 
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // 성공 및 실패한 신청의 수를 추적하기 위한 리스트
        List<String> successMessages = Collections.synchronizedList(new ArrayList<>());
        List<String> failureMessages = Collections.synchronizedList(new ArrayList<>());

        for (long i = 1L; i <= threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    EnrollmentDto.EnrollmentRequestDto requestDto = new EnrollmentDto.EnrollmentRequestDto();
                    requestDto.setUserId(userId);
                    requestDto.setCourseId(course.getId());
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

