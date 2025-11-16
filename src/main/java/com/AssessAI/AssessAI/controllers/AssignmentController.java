package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.models.Assignment;
import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.payloads.ClassroomDTO;
import com.AssessAI.AssessAI.payloads.FetchQuestionsDetailsDTO;
import com.AssessAI.AssessAI.service.AssignmentService;
import com.AssessAI.AssessAI.service.QuizAIService;
import com.AssessAI.AssessAI.utils.AuthUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private QuizAIService quizAIService;

    @Autowired
    private AuthUtil authUtil;

    // create assignment
    @PostMapping("/create")
    public ResponseEntity<String> createAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        try {
            Long savedAssignmentId = assignmentService.saveAssignment(assignmentDTO);
            // Quiz generate करें और JSON वापस भेजें
            String quizJson = quizAIService.generateAndReturnQuizJson(
                    assignmentDTO.getTitle(),
                    assignmentDTO.getDescription(),
                    assignmentDTO.getDifficulty(),
                    assignmentDTO.getClassroomCode(),
                    savedAssignmentId,
                    assignmentDTO.getNumMcqs(),
                    assignmentDTO.getNumWriting()
            );
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(quizJson);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    // get assignment by id
    @GetMapping("/{assignmentId}")
    public ResponseEntity<FetchQuestionsDetailsDTO> getAssignmentById(@PathVariable Long assignmentId) {
        FetchQuestionsDetailsDTO fetchQuestionsDetailsDTO = assignmentService.getAssignmentById(assignmentId);
        return new ResponseEntity<>(fetchQuestionsDetailsDTO, HttpStatus.OK);
    }


    // update the assignment
    @PutMapping("/update/{assignmentId}")
    public ResponseEntity<AssignmentDTO> updateAssignment(@PathVariable Long assignmentId ,
                                                          @RequestBody  AssignmentDTO assignmentDTO) {
        AssignmentDTO updateAssignment = assignmentService.updateAssignment(assignmentId, assignmentDTO);
        return new ResponseEntity<AssignmentDTO>(updateAssignment, HttpStatus.OK);
    }

    // delete assignment
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable Long assignmentId) {
        String result = assignmentService.deleteAssignment(assignmentId);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    // get all assignments of a specific classroom
    @GetMapping("/classroom/{classroomCode}")
    public ResponseEntity<List<AssignmentDTO>> allAssignments(@PathVariable String classroomCode) {
        List<AssignmentDTO> allAssignments = assignmentService.getAllAssignmentOfClassroom(classroomCode);
        return new ResponseEntity<List<AssignmentDTO>>(allAssignments, HttpStatus.OK);
    }

    @GetMapping("/classroom/{classroomId}/attempted-assignments")
    public List<Assignment> getAttemptedAssignments(
            @PathVariable Long classroomId) {

        Long studentId = authUtil.loggedInStudentId();
        return assignmentService.getAttemptedAssignments(studentId, classroomId);
    }

    @GetMapping("/test/{assignmentId}")
    public Long fetchTestId(@PathVariable Long assignmentId) {
        return assignmentService.fetchTestId(assignmentId);
    }

}
