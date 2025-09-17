package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Result;

import java.util.List;
import java.util.Optional;

public interface ResultService {
    Result saveResult(Result result);
    Result updateResult(Long id, Result result);
    void deleteResult(Long id);
    Optional<Result> getResultById(Long id);
    List<Result> getAllResults();
    List<Result> getResultsByStudentId(Long studentId);
    List<Result> getResultsByClassroomId(Long classroomId);
    List<Result> getResultsByAssignmentId(Long assignmentId);
    List<Result> getResultsByAssessmentId(Long assessmentId);
}
