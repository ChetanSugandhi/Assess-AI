package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentId(Long studentId);
    List<Result> findByClassroomId(Long classroomId);
    List<Result> findByAssignmentId(Long assignmentId);
    List<Result> findByAssessmentId(Long assessmentId);
}
