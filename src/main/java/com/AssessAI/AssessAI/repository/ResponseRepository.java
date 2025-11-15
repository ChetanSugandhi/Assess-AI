package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Assignment;
import com.AssessAI.AssessAI.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    @Query("""
       SELECT DISTINCT r.assignment
       FROM Response r
       WHERE r.student.stId = :studentId
       AND r.assignment.classroom.cId = :classroomId
       """)
    List<Assignment> findAttemptedAssignmentsInClassroom(Long studentId, Long classroomId);


    @Query("""
       SELECT r 
       FROM Response r 
       WHERE r.student.stId = :studentId
       AND r.assignment.asgnId = :assignmentId
       """)
    List<Response> findResponsesForAssignment(Long studentId, Long assignmentId);





}
