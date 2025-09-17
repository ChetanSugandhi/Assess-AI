package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Teacher;
import com.AssessAI.AssessAI.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements com.AssessAI.AssessAI.service.TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher updateTeacher(Long id, Teacher teacher) {
        Optional<Teacher> existing = teacherRepository.findById(id);
        if (existing.isPresent()) {
            Teacher t = existing.get();
            t.setFullName(teacher.getFullName());
            t.setSpecialization(teacher.getSpecialization());
            t.setQualification(teacher.getQualification());
            t.setContactNo(teacher.getContactNo());
            t.setUser(teacher.getUser());
            return teacherRepository.save(t);
        }
        return null; // ya exception throw karo
    }

    @Override
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> getTeacherByUserId(Long userId) {
        return teacherRepository.findByUserId(userId);
    }
}
