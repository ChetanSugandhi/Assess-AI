package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {
    // Optional: custom query if needed
    Paragraph findByQuestionId(Long questionId);
}
