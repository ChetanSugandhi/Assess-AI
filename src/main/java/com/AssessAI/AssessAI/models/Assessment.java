package com.AssessAI.AssessAI.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

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

    private String videoLink;
    @Size(max = 1000, message = "Description maximum 1000 characters")
    private String videoDescription;


    private String audioLink;
    @Size(max = 1000, message = "Description maximum 1000 characters")
    private String audioDescription;


    private String textLink;
    @Size(max = 1000, message = "Description maximum 1000 characters")
    private String textDescription;


    @OneToOne
    @JoinColumn(name = "classroom_id")
    @JsonBackReference
    private Classroom classroom;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonBackReference
    private Set<Test> tests = new HashSet<>();
}
