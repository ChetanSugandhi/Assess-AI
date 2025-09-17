package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assessment;
import java.util.List;
import java.util.Optional;

public interface AssessmentService {
    Assessment saveAssessment(Assessment assessment);
    Optional<Assessment> getAssessmentById(Long id);
    List<Assessment> getAllAssessments();
    Assessment updateAssessment(Long id, Assessment updatedAssessment);
    void deleteAssessment(Long id);
}
