package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.payloads.ClassroomDTO;
import com.AssessAI.AssessAI.service.AssignmentService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // create assignment
    @PostMapping("/create")
    public ResponseEntity<AssignmentDTO> createAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        AssignmentDTO savedAssignment = assignmentService.saveAssignment(assignmentDTO);
        return new ResponseEntity<AssignmentDTO>(savedAssignment, HttpStatus.CREATED);
    }

    // get assignment by id
    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentDTO> getAssignmentById(@PathVariable Long assignmentId) {
        AssignmentDTO assignmentDTO = assignmentService.getAssignmentById(assignmentId);
        return new ResponseEntity<AssignmentDTO>(assignmentDTO, HttpStatus.OK);
    }

    // update the assignment
    @PutMapping("/update/{assignmentId}")
    public ResponseEntity<AssignmentDTO> updateAssignment(@PathVariable Long assignmentId ,
                                                          @RequestBody  AssignmentDTO assignmentDTO) {
        AssignmentDTO updateAssignment = assignmentService.updateAssignment(assignmentId, assignmentDTO);
        return new ResponseEntity<AssignmentDTO>(updateAssignment, HttpStatus.OK);
    }
}
