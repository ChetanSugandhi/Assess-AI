package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Classroom;
import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.payloads.ClassroomDTO;
import com.AssessAI.AssessAI.payloads.StudentDTO;

import java.util.List;
import java.util.Set;

public interface ClassroomService {
    Classroom saveClassroom(Long id, ClassroomDTO classroom);
    Classroom getClassroomById(Long id);
    List<ClassroomDTO> getAllClassrooms();
//    ClassroomDTO updateClassroom(Long id, ClassroomDTO updatedClassroomDTO);
    String deleteClassroom(Long id);

    // get all classrooms of a teacher
    List<ClassroomDTO> getAllClassroomsOfTeacherByTeacherId(Long teacherId);

    // get all classrooms of a student
    Set<Classroom> getAllClassroomsOfStudentByStudentId(Long studentId);

    String joinClassroomByClassroomCode(String classroomCode);

    String removeStudentFromClassroom(Long classroomId, Long studentId);

    List<AssignmentDTO> getAllAssignmentOfClassroom(Long classroomId);

    List<StudentDTO> getAllStudentsOfClassroom(Long classroomId);
}
