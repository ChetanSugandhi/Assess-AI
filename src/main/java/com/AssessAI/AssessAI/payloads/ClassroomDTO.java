package com.AssessAI.AssessAI.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDTO {

    private Long classroomId;
    private String className;
    private String subject;
    private String classroomCode;


    private TeacherDTO teacher;
    private Set<StudentDTO> students = new HashSet<>();
    private Set<AssignmentDTO> assignments = new HashSet<>();
    private Set<AssessmentDTO> assessments = new HashSet<>();

}
