package com.AssessAI.AssessAI.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mcqs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MCQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mcqId;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
