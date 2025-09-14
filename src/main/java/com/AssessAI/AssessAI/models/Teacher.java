package com.AssessAI.AssessAI.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tchrId;

    private String fullName;
    private String specialization;
    private String qualification;
    private String contactNo;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "teacher")
    private Set<Classroom> classrooms = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<Feedback> feedbacks = new HashSet<>();
}
