package com.example.hhplus2week.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "courses")
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    // 수강 가능 인원
    @Column(nullable = false)
    private Long capacity;

    // 현재 신청 인원
    @Column
    private Long enrollCount = 0L;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    @Builder
    public Course(Long id, String name, Long capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public void increaseEnrollment() {
        if(this.enrollCount < this.capacity) {
            this.enrollCount ++;
        } else {
            throw new IllegalStateException("해당 강의 만석");
        }
    }

    public void decreaseCapacity() {
        if (this.capacity > 0) {
            this.capacity--;
        }
    }

    public boolean checkEnrollment() {
        return this.enrollCount < this.capacity;
    }

}
