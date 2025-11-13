package com.AssessAI.AssessAI.payloads;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ParagraphResult {
    private int score;

    @Column(columnDefinition = "TEXT")
    private String evaluation;
}
