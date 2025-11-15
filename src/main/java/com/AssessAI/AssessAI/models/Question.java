package com.AssessAI.AssessAI.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(columnDefinition = "TEXT")
    private String text;
    private Integer marks;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @JsonBackReference
    private Test test;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private MCQ mcq;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Paragraph para;

    @OneToMany(mappedBy = "question")
    @JsonManagedReference
    private Set<Response> responses = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    @JsonBackReference
    private Assignment assignment;

}
