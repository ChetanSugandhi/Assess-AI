package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.payloads.ClassroomDTO;

import java.util.List;

public interface ClassroomService {
    ClassroomDTO saveClassroom(ClassroomDTO classroom);
    ClassroomDTO getClassroomById(Long id);
    List<ClassroomDTO> getAllClassrooms();
    ClassroomDTO updateClassroom(Long id, ClassroomDTO updatedClassroomDTO);
    String deleteClassroom(Long id);

    // get all classrooms of a teacher
    List<ClassroomDTO> getAllClassroomsOfTeacherByTeacherId(Long teacherId);

    // get all classrooms of a student
    List<ClassroomDTO> getAllClassroomsOfStudentByStudentId(Long studentId);
}
