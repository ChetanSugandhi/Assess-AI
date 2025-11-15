package com.AssessAI.AssessAI.payloads;

import lombok.Data;

import java.util.List;

@Data
public class AssignmentAttemptDTO {
    private Long assignmentId;
    private String title;
    private List<QuestionAnswerDTO> questions;
}
