package com.AssessAI.AssessAI.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;

    private ClassroomDTO classroom;
}
