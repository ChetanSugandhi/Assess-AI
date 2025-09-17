package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Classroom;
import java.util.List;
import java.util.Optional;

public interface ClassroomService {
    Classroom saveClassroom(Classroom classroom);
    Optional<Classroom> getClassroomById(Long id);
    List<Classroom> getAllClassrooms();
    Classroom updateClassroom(Long id, Classroom updatedClassroom);
    void deleteClassroom(Long id);
}
