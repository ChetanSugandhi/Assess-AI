package com.AssessAI.AssessAI.payloads;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@NoArgsConstructor
public class StudentAnswerDTO {
    private Long questionId;

    @Column(columnDefinition = "TEXT")
    private String answer;
}
