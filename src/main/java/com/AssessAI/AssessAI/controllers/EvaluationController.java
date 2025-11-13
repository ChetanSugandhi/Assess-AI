package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.payloads.StudentAnswerDTO;
import com.AssessAI.AssessAI.payloads.TestResultResponseDTO;
import com.AssessAI.AssessAI.service.EvaluationService;
import com.AssessAI.AssessAI.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluate")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/{testId}")
    public TestResultResponseDTO evaluate( @PathVariable Long testId,
                                           @RequestBody List<StudentAnswerDTO> answers ) throws Exception {

        Long studentId = authUtil.loggedInStudentId();

        return evaluationService.evaluateTest(testId, studentId, answers);
    }
}