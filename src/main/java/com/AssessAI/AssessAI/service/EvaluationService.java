package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.payloads.StudentAnswerDTO;
import com.AssessAI.AssessAI.payloads.TestResultResponseDTO;

import java.util.List;

public interface EvaluationService {
    TestResultResponseDTO evaluateTest(Long testId, Long studentId, List<StudentAnswerDTO> answers) throws Exception;
}
