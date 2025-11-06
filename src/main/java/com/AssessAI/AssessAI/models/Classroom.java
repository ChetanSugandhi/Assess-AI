package com.AssessAI.AssessAI.models;

import com.AssessAI.AssessAI.payloads.AssessmentDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classrooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cId;

    private String className;
    private String subject;
    private String classroomCode;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonBackReference
    private Teacher teacher;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "student_classroom",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonManagedReference
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Assignment> assignments = new HashSet<>();

    @OneToOne(mappedBy = "classroom", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Assessment assessment;
}
