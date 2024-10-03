package com.example.hhplus2week.service.course;

import com.example.hhplus2week.domain.Course;
import com.example.hhplus2week.dto.course.CourseDto;
import com.example.hhplus2week.repository.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    /**
     * 특강 등록 확인 테스트
     */
    @DisplayName("특강이 등록되었는지 확인")
    @Test
    public void registerCourseTest() {
        Long courseId = 1L;
        String name = "spring";
        Long capacity = 30L;

        CourseDto.CourseRequestDto requestDto = new CourseDto.CourseRequestDto();
        requestDto.setCourseId(1L);
        requestDto.setName("spring");

        Course rgCourse = Course.builder()
                .id(courseId)
                .name(name)
                .capacity(capacity)
                .build();

        when(courseRepository.save(any(Course.class)))
                .thenReturn(rgCourse);

        CourseDto.CourseResponseDto responseDto = courseService.registerCourse(requestDto);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getCourseId());
        assertEquals("spring", responseDto.getName());
        assertEquals(30L, responseDto.getCapacity());

    }

}