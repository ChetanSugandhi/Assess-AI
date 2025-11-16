package com.AssessAI.AssessAI.payloads;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    private Long qId;
    private String text;

    private String type; // MCQ or PARAGRAPH

    private MCQDTO mcq; // only for MCQ
}
