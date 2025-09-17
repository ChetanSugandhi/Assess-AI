package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    // Optional: custom queries if needed
}
