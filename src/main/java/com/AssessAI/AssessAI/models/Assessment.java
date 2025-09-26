package com.AssessAI.AssessAI.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "assessments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long asmtId;

    private String name;
    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonBackReference
    private Classroom classroom;

    @OneToOne(mappedBy = "assessment")
    @JsonBackReference
    private Test test;

    @OneToMany(mappedBy = "assessment")
    @JsonManagedReference
    private Set<Result> results = new HashSet<>();
}
