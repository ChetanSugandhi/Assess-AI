package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assignment;
import com.AssessAI.AssessAI.repository.AssignmentRepository;
import com.AssessAI.AssessAI.service.AssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public Assignment saveAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Override
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public Assignment updateAssignment(Long id, Assignment updatedAssignment) {
        return assignmentRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updatedAssignment.getTitle());
                    existing.setDescription(updatedAssignment.getDescription());
                    existing.setDueDate(updatedAssignment.getDueDate());
                    existing.setClassroom(updatedAssignment.getClassroom());
                    return assignmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Assignment not found with id " + id));
    }

    @Override
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found with id " + id);
        }
        assignmentRepository.deleteById(id);
    }
}
