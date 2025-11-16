package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assignment;
import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.payloads.FetchQuestionsDetailsDTO;
import com.AssessAI.AssessAI.payloads.QuestionAnswerDTO;

import java.util.List;

public interface AssignmentService {
    Long saveAssignment(AssignmentDTO assignment);
    FetchQuestionsDetailsDTO getAssignmentById(Long id);
    AssignmentDTO updateAssignment(Long id, AssignmentDTO updatedAssignment);
    String deleteAssignment(Long id);

    List<AssignmentDTO> getAllAssignmentOfClassroom(String classroomCode);

    List<Assignment> getAttemptedAssignments(Long studentId, Long classroomId);


    String getAttemptedAssignmentsQuestionAnswers(Long studentId, Long classroomId) throws Exception;

    Long fetchTestId(Long assignmentId);
}
