package com.AssessAI.AssessAI.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "assignments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long asgnId;

    private String title;
    private String description;
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonBackReference
    private Classroom classroom;

    @OneToOne(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Test test;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Result> results = new HashSet<>();
}
