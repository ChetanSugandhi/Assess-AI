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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        if(!classroomRepository.existsByClassroomCode(assignmentDTO.getClassroomCode())) {
            throw new IllegalArgumentException("Classroom with classroom code : " + assignmentDTO.getClassroomCode() + " not found..!!");
        }

        Assignment newAssignemnt = new Assignment();
        newAssignemnt.setTitle(assignmentDTO.getTitle());
        newAssignemnt.setDescription(assignmentDTO.getDescription());
        newAssignemnt.setDueDate(assignmentDTO.getDueDate());

        Optional<Classroom> fetchClassroom = classroomRepository.findByClassroomCode(assignmentDTO.getClassroomCode());
        if(fetchClassroom.isPresent()) {
            Classroom fetchClassroomConvert = fetchClassroom.get();
            newAssignemnt.setClassroom(fetchClassroomConvert);
        }


        Assignment saveAssignment = assignmentRepository.save(newAssignemnt);

        return modelMapper.map(saveAssignment, AssignmentDTO.class);
    }

    @Override
    public AssignmentDTO getAssignmentById(Long id) {
        Assignment findAssignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id : " + id));


        return modelMapper.map(findAssignment, AssignmentDTO.class);
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

    @Override
    public List<AssignmentDTO> getAllAssignmentOfClassroom(String classroomCode) {

        if(!classroomRepository.existsByClassroomCode(classroomCode)) {
            throw new IllegalArgumentException("Classroom not found with classroom code : " + classroomCode);
        }

        Optional<Classroom> fetch = classroomRepository.findByClassroomCode(classroomCode);
        Classroom fetchClassroom = null;
        if(fetch.isPresent()) {
            fetchClassroom = fetch.get();
        }



        Set<Assignment> fetchAssignment = fetchClassroom.getAssignments();
        List<AssignmentDTO> assignmentDTOS = new ArrayList<>();

        for(Assignment eachAssignment : fetchAssignment) {
            AssignmentDTO assignmentDTO = modelMapper.map(eachAssignment, AssignmentDTO.class);
            assignmentDTOS.add(assignmentDTO);
        }

        return assignmentDTOS;
    }

}
