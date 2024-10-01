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

        Course course = courseRepository.findByIdWithLock(courseId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));


        // 특강 신청 가능한 인원 확인
        long enrollmentCount = enrollmentRepository.countByCourse(course);
        if(enrollmentCount >= 30) {
            throw new IllegalArgumentException("수업 가능 인원이 다 찼습니다.");
        }

        // 특강 중복 신청 확인
        boolean alreadyRegisterCourse = enrollmentRepository.existsByUserAndCourse(user, course);
        if(alreadyRegisterCourse) {
            throw new IllegalArgumentException("유저가 이미 수업을 등록했습니다.");
        }

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();

        enrollmentRepository.save(enrollment);

        return new EnrollmentDto.EnrollmentResponseDto(
                userId, courseId, "수강 신청 완료");
    }

    /**
     * 특강이 신청 가능한지 아닌지 확인
     */

}


