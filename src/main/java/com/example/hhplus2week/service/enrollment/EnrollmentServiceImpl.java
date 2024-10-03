package com.example.hhplus2week.service.enrollment;

import com.example.hhplus2week.domain.Course;
import com.example.hhplus2week.domain.Enrollment;
import com.example.hhplus2week.domain.User;
import com.example.hhplus2week.dto.enrollment.EnrollmentDto;
import com.example.hhplus2week.repository.CourseRepository;
import com.example.hhplus2week.repository.EnrollmentRepository;
import com.example.hhplus2week.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentServiceImpl implements EnrollmentService{


    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;


    /**
     * 특강을 신청
     */
    @Transactional
    public EnrollmentDto.EnrollmentResponseDto registerEnrollment(EnrollmentDto.EnrollmentRequestDto requestDto) {
        Long userId = requestDto.getUserId();
        Long courseId = requestDto.getCourseId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 비관적 락을 걸어 강의 엔티티를 가져옴
        Course course = courseRepository.findByIdWithLock(courseId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

        checkEnrollmentAvailability(course);
        checkDuplicateEnrollment(user, course);

        // 수강 신청 처리
        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();

        enrollmentRepository.save(enrollment);

        course.increaseEnrollment();
        courseRepository.save(course);

        return new EnrollmentDto.EnrollmentResponseDto(
                userId, courseId, course.getCapacity(),course.getEnrollCount());
    }

    // 특강 신청 가능 여부 확인
    private void checkEnrollmentAvailability(Course course) {
        if (!course.checkEnrollment()) {
            throw new IllegalArgumentException("수업 가능 인원이 다 찼습니다.");
        }
    }

    // 중복 신청 확인
    private void checkDuplicateEnrollment(User user, Course course) {
        boolean alreadyEnrolled = enrollmentRepository.existsByUserAndCourse(user, course);
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("유저가 이미 수업을 등록했습니다.");
        }
    }

}


