package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assessment;
import com.AssessAI.AssessAI.payloads.AssessmentDTO;

import java.util.List;
import java.util.Optional;

public interface AssessmentService {
    AssessmentDTO saveAssessment(AssessmentDTO assessmentDTO);
    AssessmentDTO getAssessmentOfClass(String classroomCode);
    String deleteAssessment(Long id);
}
