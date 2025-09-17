package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assignment;
import java.util.List;
import java.util.Optional;

public interface AssignmentService {
    Assignment saveAssignment(Assignment assignment);
    Optional<Assignment> getAssignmentById(Long id);
    List<Assignment> getAllAssignments();
    Assignment updateAssignment(Long id, Assignment updatedAssignment);
    void deleteAssignment(Long id);
}
