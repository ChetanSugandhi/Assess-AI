package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
