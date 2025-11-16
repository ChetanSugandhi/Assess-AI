package com.AssessAI.AssessAI.payloads;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MCQDTO {
    private String option1;
    private String option2;
    private String option3;
    private String option4;
//    private String correctAnswer;
}
