package com.AssessAI.AssessAI.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qId;

    private String text;
    private Integer marks;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @OneToOne(mappedBy = "question")
    private MCQ mcq;

    @OneToOne(mappedBy = "question")
    private Paragraph para;

    @OneToMany(mappedBy = "question")
    private Set<Response> responses = new HashSet<>();
}
