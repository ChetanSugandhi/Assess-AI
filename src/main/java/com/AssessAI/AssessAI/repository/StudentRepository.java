package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEnrollmentNo(String enrollmentNo);
    Optional<Student> findByUserId(Long userId);
}
