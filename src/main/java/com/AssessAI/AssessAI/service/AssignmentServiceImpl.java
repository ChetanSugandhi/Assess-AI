package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assignment;
import com.AssessAI.AssessAI.models.Classroom;
import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.repository.AssignmentRepository;
import com.AssessAI.AssessAI.repository.ClassroomRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    // create (save) assignment
    @Override
    public AssignmentDTO saveAssignment(AssignmentDTO assignmentDTO) {
        Assignment assignment = modelMapper.map(assignmentDTO, Assignment.class);

        // check kra ki classroom hai ki nhi..
        if (assignmentDTO.getClassroom() != null && assignmentDTO.getClassroom().getClassroomId() != null) {
            Classroom classroom = classroomRepository.findById(assignmentDTO.getClassroom().getClassroomId())
                    .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + assignmentDTO.getClassroom().getClassroomId()));
            assignment.setClassroom(classroom);
        }


        Assignment saveAssignment = assignmentRepository.save(assignment);

        return modelMapper.map(saveAssignment, AssignmentDTO.class);
    }

    @Override
    public AssignmentDTO getAssignmentById(Long id) {
        Assignment findAssignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id : " + id));


        return modelMapper.map(findAssignment, AssignmentDTO.class);
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    @Transactional
    public AssignmentDTO updateAssignment(Long id, AssignmentDTO assignmentDTO) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + id));

        // Update basic fields
        if (assignmentDTO.getTitle() != null)
            assignment.setTitle(assignmentDTO.getTitle());
        if (assignmentDTO.getDescription() != null)
            assignment.setDescription(assignmentDTO.getDescription());
        if (assignmentDTO.getDueDate() != null)
            assignment.setDueDate(assignmentDTO.getDueDate());

        // Update classroom if provided
        if (assignmentDTO.getClassroom() != null && assignmentDTO.getClassroom().getClassroomId() != null) {
            Classroom classroom = classroomRepository.findById(assignmentDTO.getClassroom().getClassroomId())
                    .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + assignmentDTO.getClassroom().getClassroomId()));
            assignment.setClassroom(classroom);
        }

        Assignment updated = assignmentRepository.save(assignment);
        return modelMapper.map(updated, AssignmentDTO.class);
    }

    @Override
    @Transactional
    public String deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        assignmentRepository.delete(assignment);
        return "Assignment deleted successfully";
    }

}
