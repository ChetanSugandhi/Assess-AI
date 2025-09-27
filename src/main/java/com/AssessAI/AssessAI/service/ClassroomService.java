package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Classroom;
import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.payloads.ClassroomDTO;

import java.util.List;

public interface ClassroomService {
    Classroom saveClassroom(ClassroomDTO classroom);
    Classroom getClassroomById(Long id);
    List<ClassroomDTO> getAllClassrooms();
//    ClassroomDTO updateClassroom(Long id, ClassroomDTO updatedClassroomDTO);
    String deleteClassroom(Long id);

    // get all classrooms of a teacher
    List<ClassroomDTO> getAllClassroomsOfTeacherByTeacherId(Long teacherId);

    // get all classrooms of a student
    List<ClassroomDTO> getAllClassroomsOfStudentByStudentId(Long studentId);

    String joinClassroomByClassroomCode(String classroomCode);

    String removeStudentFromClassroom(Long classroomId, Long studentId);

    List<AssignmentDTO> getAllAssignmentOfClassroom(String classroomCode);
}
