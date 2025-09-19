package com.AssessAI.AssessAI.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "responses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long respId;

    private String answer;
    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test; // optional: evaluator

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
