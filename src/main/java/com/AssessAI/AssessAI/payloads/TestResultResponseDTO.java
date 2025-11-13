package com.AssessAI.AssessAI.payloads;

import lombok.Data;

import java.util.List;

@Data
public class TestResultResponseDTO {
    private int totalScore;
    private int maxScore;
    private List<QuestionResultDTO> questionResults;
}
