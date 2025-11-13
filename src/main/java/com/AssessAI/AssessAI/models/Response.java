package com.AssessAI.AssessAI.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(columnDefinition = "TEXT")
    private String answer;
    private Boolean isCorrect;
    private Integer paragraphScore;

    @Column(columnDefinition = "TEXT")
    private String paragraphEvaluation;
    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private Student student;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @JsonBackReference
    private Test test; // optional: evaluator

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private Question question;
}
