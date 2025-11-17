package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.models.Assignment;
import com.AssessAI.AssessAI.payloads.QuestionAnswerDTO;
import com.AssessAI.AssessAI.service.AssignmentService;
import com.AssessAI.AssessAI.service.FeedbackService;
import com.AssessAI.AssessAI.utils.AuthUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    private AuthUtil authUtil;

    @GetMapping("/evaluate/{classroomId}")
    public ResponseEntity<?>  getFeedback(@PathVariable Long classroomId) throws Exception {

        Long studentId = authUtil.loggedInStudentId();

       String aiText = assignmentService.getAttemptedAssignmentsQuestionAnswers(studentId, classroomId);

        Map<String, Object> parsed = feedbackService.parseFeedback(aiText);

        return ResponseEntity.ok(parsed);
    }

}
