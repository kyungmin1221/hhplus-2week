package com.example.hhplus2week.repository;

import com.example.hhplus2week.domain.Course;
import com.example.hhplus2week.domain.Enrollment;
import com.example.hhplus2week.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    long countByCourse(Course course);
    boolean existsByUserAndCourse(User user, Course course);
    List<Enrollment> findByUser(User user);
}
