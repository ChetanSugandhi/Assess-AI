package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.payloads.AssessmentDTO;
import com.AssessAI.AssessAI.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @PostMapping("/create")
    public ResponseEntity<AssessmentDTO> createAssessment(@RequestBody AssessmentDTO assessmentDTO) {
        AssessmentDTO result = assessmentService.saveAssessment(assessmentDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{classroomCode}")
    public ResponseEntity<AssessmentDTO> getAllAssessment(@PathVariable String classroomCode) {
        AssessmentDTO result = assessmentService.getAssessmentOfClass(classroomCode);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{assessmentId}")
    public String deleteAssessment(@PathVariable Long assessmentId) {
        return assessmentService.deleteAssessment(assessmentId);
    }

}
