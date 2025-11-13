package com.AssessAI.AssessAI.payloads;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class QuestionResultDTO {
        private Long questionId;

        @Column(columnDefinition = "TEXT")
        private String questionText;
        private String type;

        // for MCQ
        private String studentAnswer;
        private String correctAnswer;
        private boolean isCorrect;

        // for Paragraph
        @Column(columnDefinition = "TEXT")
        private String studentParagraph;

        private int paragraphScore;
        private String correctnessLevel;

        @Column(columnDefinition = "TEXT")
        private String paragraphEvaluation;
}