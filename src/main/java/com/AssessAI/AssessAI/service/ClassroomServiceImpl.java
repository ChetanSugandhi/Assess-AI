package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.*;
import com.AssessAI.AssessAI.payloads.AssessmentDTO;
import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.payloads.ClassroomDTO;
import com.AssessAI.AssessAI.payloads.StudentDTO;
import com.AssessAI.AssessAI.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    // save/create classroom
    @Override
    public ClassroomDTO saveClassroom(ClassroomDTO classroomDTO) {
        Classroom classroom = modelMapper.map(classroomDTO, Classroom.class);

        // Save entity
        Classroom savedClassroom = classroomRepository.save(classroom);

        // Convert back Entity -> DTO
        return modelMapper.map(savedClassroom, ClassroomDTO.class);
    }

    // get classroom by id
    @Override
    public ClassroomDTO getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom with Id" + id + " not found..!!"));
        return  modelMapper.map(classroom, ClassroomDTO.class);
    }

    // show all classrooms
    @Override
    public List<ClassroomDTO> getAllClassrooms() {
        List<Classroom> allClassroom = classroomRepository.findAll();
        List<ClassroomDTO> classroomDTOS = new ArrayList<>();

        for(Classroom eachClassroom  : allClassroom) {
            ClassroomDTO classroomDTO = modelMapper.map(eachClassroom, ClassroomDTO.class);
            classroomDTOS.add(classroomDTO);
        }
        return classroomDTOS;
    }


    // update in classroom
    @Override
    @Transactional
    public ClassroomDTO updateClassroom(Long id, ClassroomDTO updatedClassroom) {
        Classroom findClassroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom with Id" + id + " not found..!!"));

        // basic details set kri..
        findClassroom.setClassName(updatedClassroom.getClassName());
        findClassroom.setSubject(updatedClassroom.getSubject());
        findClassroom.setClassroomCode(updatedClassroom.getClassroomCode());

        // teacher find kra..
        if(updatedClassroom.getTeacher() != null) {
            Teacher teacher = teacherRepository.findById(updatedClassroom.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher with id not found.." + updatedClassroom.getTeacher().getId()));

            findClassroom.setTeacher(teacher);
        }

        // student ko extract kra from updatedClassroom se and usko set kra classroom mein..
        Set<Student> students = new HashSet<>();
        for(StudentDTO eachStudentDTO : updatedClassroom.getStudents()) {
            Student checkStudent = studentRepository.findById(eachStudentDTO.getId())
                    .orElseThrow(() -> new RuntimeException("No Student found with id : " + eachStudentDTO.getId()));
            students.add(checkStudent);
        }
        findClassroom.setStudents(students);


        // assignment set kro..
        Set<Assignment> assignments = new HashSet<>();
        for(AssignmentDTO eachAssignment : updatedClassroom.getAssignments()) {
            Assignment assignment = assignmentRepository.findById(eachAssignment.getId())
                    .orElseThrow(() -> new RuntimeException("No Assignment found with id : " + eachAssignment.getId()));
            assignments.add(assignment);
        }

        findClassroom.setAssignments(assignments);



        // assessment set kro..
        Set<Assessment> assessments = new HashSet<>();
        for(AssessmentDTO eachAssessment : updatedClassroom.getAssessments()) {
            Assessment assessment = assessmentRepository.findById(eachAssessment.getId())
                    .orElseThrow(() -> new RuntimeException("No Assessment found with id : " + eachAssessment.getId()));
            assessments.add(assessment);
        }

        findClassroom.setAssessments(assessments);


        // save kra classroom ko
        Classroom savedClassroom = classroomRepository.save(findClassroom);

        // return kra..
        return modelMapper.map(savedClassroom, ClassroomDTO.class);
    }


    // delete the classroom by id
    @Override
    @Transactional
    public String deleteClassroom(Long id) {
        Classroom findClassroom = classroomRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Classroom with Id not found : " + id));

        // saare student ke isme se classroom ko hta diye..
        for(Student eachStudent : findClassroom.getStudents()) {
            eachStudent.getClassrooms().remove(findClassroom);
        }

        // classroom ko delete kra toh usse related assignment and assessment bhi delete becuase of cascading..
        classroomRepository.delete(findClassroom);
        return "Successfully deleted classroom..!!";
    }

    // get all classroom of teacher by teacher Id
    @Override
    public List<ClassroomDTO> getAllClassroomsOfTeacherByTeacherId(Long teacherId) {
        Teacher findTeacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher with teacher ID : " + teacherId + " not found..!!"));

        Set<Classroom> fetchClassrooms = findTeacher.getClassrooms();

        List<ClassroomDTO> classroomDTOS = new ArrayList<>();

        for(Classroom eachClassroom : fetchClassrooms) {
            ClassroomDTO classroomDTO = modelMapper.map(eachClassroom, ClassroomDTO.class);
            classroomDTOS.add(classroomDTO);
        }

        return classroomDTOS;
    }
}
