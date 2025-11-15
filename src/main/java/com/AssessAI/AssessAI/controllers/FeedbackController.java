package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.models.Assignment;
import com.AssessAI.AssessAI.payloads.QuestionAnswerDTO;
import com.AssessAI.AssessAI.service.AssignmentService;
import com.AssessAI.AssessAI.utils.AuthUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AuthUtil authUtil;

    @GetMapping("/evaluate/{classroomId}")
    public String getFeedback(@PathVariable Long classroomId) throws Exception {

        Long studentId = authUtil.loggedInStudentId();

       return assignmentService.getAttemptedAssignmentsQuestionAnswers(studentId, classroomId);
    }

}
