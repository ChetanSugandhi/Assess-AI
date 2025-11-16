package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.assignment.asgnId = :assignmentId")
    List<Question> findQuestionsByAssignmentId(@Param("assignmentId") Long assignmentId);

}
