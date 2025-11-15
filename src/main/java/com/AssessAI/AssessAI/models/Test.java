package com.AssessAI.AssessAI.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tId;

    private String testName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToOne
    @JoinColumn(name = "assignment_id")
    @JsonBackReference
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    @JsonIgnore
    private Assessment assessment;

    @OneToMany(mappedBy = "test")
    @JsonIgnore
    private Set<Question> questions = new HashSet<>();


    @OneToMany(mappedBy = "test")
    @JsonIgnore
    private Set<Response> responses = new HashSet<>();
}
