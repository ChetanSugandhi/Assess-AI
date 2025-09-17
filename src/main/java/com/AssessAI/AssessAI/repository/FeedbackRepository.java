package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Example: Optional<List<Feedback>> findByStudentId(Long studentId);
}
