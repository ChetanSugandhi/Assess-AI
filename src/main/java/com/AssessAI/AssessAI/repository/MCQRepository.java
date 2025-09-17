package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.MCQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MCQRepository extends JpaRepository<MCQ, Long> {
    // Agar custom queries chahiye toh yaha likh sakte ho
}
