package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student saveStudent(Student student);
    Student updateStudent(Long id, Student student);
    void deleteStudent(Long id);
    Optional<Student> getStudentById(Long id);
    List<Student> getAllStudents();
    Optional<Student> getStudentByEnrollmentNo(String enrollmentNo);
    Optional<Student> getStudentByUserId(Long userId);
}
