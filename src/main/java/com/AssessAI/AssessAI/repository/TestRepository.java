package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestRepository extends JpaRepository<Test, Long> {

    @Query("SELECT t.tId FROM Test t WHERE t.assignment.asgnId = :assignmentId")
    Long findTestIdByAssignmentId(Long assignmentId);

}
