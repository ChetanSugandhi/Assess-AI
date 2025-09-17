package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Classroom;
import com.AssessAI.AssessAI.repository.ClassroomRepository;
import com.AssessAI.AssessAI.service.ClassroomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public Optional<Classroom> getClassroomById(Long id) {
        return classroomRepository.findById(id);
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom updateClassroom(Long id, Classroom updatedClassroom) {
        return classroomRepository.findById(id)
                .map(existing -> {
                    existing.setClassName(updatedClassroom.getClassName());
                    existing.setSubject(updatedClassroom.getSubject());
                    existing.setSection(updatedClassroom.getSection());
                    existing.setTeacher(updatedClassroom.getTeacher());
                    existing.setStudents(updatedClassroom.getStudents());
                    return classroomRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Classroom not found with id " + id));
    }

    @Override
    public void deleteClassroom(Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new RuntimeException("Classroom not found with id " + id);
        }
        classroomRepository.deleteById(id);
    }
}
