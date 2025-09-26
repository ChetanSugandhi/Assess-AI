package com.AssessAI.AssessAI.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paragraphs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paragraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paraId;

    private String passage;

    @OneToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private Question question;
}
