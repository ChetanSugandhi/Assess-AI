package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assignment;
import com.AssessAI.AssessAI.payloads.AssignmentDTO;

import java.util.List;

public interface AssignmentService {
    AssignmentDTO saveAssignment(AssignmentDTO assignment);
    AssignmentDTO getAssignmentById(Long id);
    AssignmentDTO updateAssignment(Long id, AssignmentDTO updatedAssignment);
    String deleteAssignment(Long id);

    List<AssignmentDTO> getAllAssignmentOfClassroom(String classroomCode);
}
