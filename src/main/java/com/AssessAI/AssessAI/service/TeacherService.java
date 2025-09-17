package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Teacher;
import com.AssessAI.AssessAI.service.TeacherService;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    Teacher saveTeacher(Teacher teacher);
    Teacher updateTeacher(Long id, Teacher teacher);
    void deleteTeacher(Long id);
    Optional<Teacher> getTeacherById(Long id);
    List<Teacher> getAllTeachers();
    Optional<Teacher> getTeacherByUserId(Long userId);
}
