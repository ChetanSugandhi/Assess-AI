package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Optional: Test ke questions fetch karne ke liye
    List<Question> findByTestQId(Long testId);
}
