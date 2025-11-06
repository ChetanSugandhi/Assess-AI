package com.AssessAI.AssessAI.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stId;

    private String fullName;
    private String enrollmentNo;
    private String contactNo;
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToMany(mappedBy = "students")
    @JsonBackReference
    private Set<Classroom> classrooms = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Result> results = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Response> responses = new HashSet<>();
}
