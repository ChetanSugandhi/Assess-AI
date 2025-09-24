package com.AssessAI.AssessAI.payloads;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate date;


    private ClassroomDTO classroom;
}
