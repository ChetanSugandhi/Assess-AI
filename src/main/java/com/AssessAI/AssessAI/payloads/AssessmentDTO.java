package com.AssessAI.AssessAI.payloads;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDTO {

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

    private String classroomCode;

}
