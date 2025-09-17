package com.AssessAI.AssessAI.services.impl;

import com.AssessAI.AssessAI.models.Student;
import com.AssessAI.AssessAI.repository.StudentRepository;
import com.AssessAI.AssessAI.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        Optional<Student> existing = studentRepository.findById(id);
        if(existing.isPresent()) {
            Student s = existing.get();
            s.setFullName(student.getFullName());
            s.setEnrollmentNo(student.getEnrollmentNo());
            s.setContactNo(student.getContactNo());
            s.setAddress(student.getAddress());
            s.setUser(student.getUser());
            return studentRepository.save(s);
        }
        return null; // ya exception throw karo
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentByEnrollmentNo(String enrollmentNo) {
        return studentRepository.findByEnrollmentNo(enrollmentNo);
    }

    @Override
    public Optional<Student> getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }
}
