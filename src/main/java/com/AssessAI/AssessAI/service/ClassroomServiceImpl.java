package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.*;
import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.payloads.ClassroomDTO;
import com.AssessAI.AssessAI.payloads.StudentDTO;
import com.AssessAI.AssessAI.payloads.UserDTO;
import com.AssessAI.AssessAI.repository.*;
import com.AssessAI.AssessAI.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AuthUtil authUtil;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    public Classroom saveClassroom(Long id, ClassroomDTO classroomDTO) {
        Classroom classroom = new Classroom();
        classroom.setClassName(classroomDTO.getClassName());
        classroom.setSubject(classroomDTO.getSubject());

        if(classroomRepository.existsByClassroomCode(classroomDTO.getClassroomCode())) {
            throw new IllegalArgumentException("Classroom code already exists");
        }

        classroom.setClassroomCode(classroomDTO.getClassroomCode());

        // Check for userId null
        if (id == null) {
            throw new RuntimeException("UserId is required for creating a classroom");
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getTeacher() != null) {
                Teacher teacher = teacherRepository.findById(user.getTeacher().getTchrId())
                        .orElseThrow(() -> new RuntimeException("Teacher record not found for the user"));
                classroom.setTeacher(teacher);
            } else {
                throw new RuntimeException("User is not linked to any teacher");
            }
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }

        return classroomRepository.save(classroom);
    }


    // get classroom by id
    @Override
    public Classroom getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom with Id" + id + " not found..!!"));
        return  classroom;
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
//    @Override
//    @Transactional
//    public ClassroomDTO updateClassroom(Long id, ClassroomDTO updatedClassroom) {
//        Classroom findClassroom = classroomRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Classroom with Id" + id + " not found..!!"));
//
//        // basic details set kri..
//        findClassroom.setClassName(updatedClassroom.getClassName());
//        findClassroom.setSubject(updatedClassroom.getSubject());
//        findClassroom.setClassroomCode(updatedClassroom.getClassroomCode());
//
//        // teacher find kra..
//        if(updatedClassroom.getTeacher() != null) {
//            Teacher teacher = teacherRepository.findById(updatedClassroom.getTeacher().getId())
//                    .orElseThrow(() -> new RuntimeException("Teacher with id not found.." + updatedClassroom.getTeacher().getId()));
//
//            findClassroom.setTeacher(teacher);
//        }
//
//        // student ko extract kra from updatedClassroom se and usko set kra classroom mein..
//        Set<Student> students = new HashSet<>();
//        for(StudentDTO eachStudentDTO : updatedClassroom.getStudents()) {
//            Student checkStudent = studentRepository.findById(eachStudentDTO.getId())
//                    .orElseThrow(() -> new RuntimeException("No Student found with id : " + eachStudentDTO.getId()));
//            students.add(checkStudent);
//        }
//        findClassroom.setStudents(students);
//
//
//        // assignment set kro..
//        Set<Assignment> assignments = new HashSet<>();
//        for(AssignmentDTO eachAssignment : updatedClassroom.getAssignments()) {
//            Assignment assignment = assignmentRepository.findById(eachAssignment.getId())
//                    .orElseThrow(() -> new RuntimeException("No Assignment found with id : " + eachAssignment.getId()));
//            assignments.add(assignment);
//        }
//
//        findClassroom.setAssignments(assignments);
//
//
//
//        // assessment set kro..
//        Set<Assessment> assessments = new HashSet<>();
//        for(AssessmentDTO eachAssessment : updatedClassroom.getAssessments()) {
//            Assessment assessment = assessmentRepository.findById(eachAssessment.getId())
//                    .orElseThrow(() -> new RuntimeException("No Assessment found with id : " + eachAssessment.getId()));
//            assessments.add(assessment);
//        }
//
//        findClassroom.setAssessments(assessments);
//
//
//        // save kra classroom ko
//        Classroom savedClassroom = classroomRepository.save(findClassroom);
//
//        // return kra..
//        return modelMapper.map(savedClassroom, ClassroomDTO.class);
//    }


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


    // get all classroom of student by student Id
    @Override
    public Set<Classroom> getAllClassroomsOfStudentByStudentId(Long studentId) {
        Student findStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student with student ID : " + studentId + " not found..!!"));

        Set<Classroom> fetchClassrooms = findStudent.getClassrooms();

//        List<ClassroomDTO> classroomDTOS = new ArrayList<>();
//
//        for(Classroom eachClassroom : fetchClassrooms) {
//            ClassroomDTO classroomDTO = modelMapper.map(eachClassroom, ClassroomDTO.class);
//            classroomDTOS.add(classroomDTO);
//        }

        return fetchClassrooms;
    }


    // join classroom with classroom code....
    @Override
    public String joinClassroomByClassroomCode(String classroomCode) {
        Student currLoggedInStudent = authUtil.getLoggedInStudent();

        Classroom fetchClassroom = classroomRepository.findByClassroomCode(classroomCode)
                .orElseThrow(() -> new RuntimeException("Classroom with classroom code : " + classroomCode + " not found..!!"));

        if(fetchClassroom.getStudents().contains(currLoggedInStudent)) {
            return "You are already in this classroom..!!";
        }

        fetchClassroom.getStudents().add(currLoggedInStudent);
        currLoggedInStudent.getClassrooms().add(fetchClassroom);

        classroomRepository.save(fetchClassroom);
        studentRepository.save(currLoggedInStudent);

        return "Successfully joined classroom with code: " + classroomCode;

    }


    // remove student from classroom
    @Override
    @Transactional
    public String removeStudentFromClassroom(Long classroomId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with student Id : " + studentId));

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom with classroom Id not found : " + classroomId));

        if (!classroom.getStudents().contains(student)) {
            throw new RuntimeException("Student is not part of this classroom.");
        }

        student.getClassrooms().remove(classroom);
        classroom.getStudents().remove(student);

        classroomRepository.save(classroom);
        studentRepository.save(student);

        return "Student with ID " + studentId + " removed successfully from Classroom " + classroomId;
    }


    // fetch all the assignments of the classroom by classroom code
    @Override
    public List<AssignmentDTO> getAllAssignmentOfClassroom(Long classroomId) {
        Classroom fetchClassroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom with classroom id not found..!!" + classroomId));

        List<AssignmentDTO> assignmentDTOS = new ArrayList<>();
        Set<Assignment> allAssignment = fetchClassroom.getAssignments();
        if(allAssignment.isEmpty()) {
            throw new RuntimeException("There is no assignment, add assignments to continue...");
        }

        for(Assignment eachAssignment : allAssignment) {
            AssignmentDTO assignmentDTO = modelMapper.map(eachAssignment, AssignmentDTO.class);
            assignmentDTOS.add(assignmentDTO);
        }

        return assignmentDTOS;
    }


    @Override
    public List<StudentDTO> getAllStudentsOfClassroom(Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("No classrooom found"));

        Set<Student> students = classroom.getStudents();

        // Convert Student entities to StudentDTOs
        List<StudentDTO> studentDTOS = students.stream().map(student -> {
            StudentDTO dto = new StudentDTO();
            dto.setId(student.getStId());
            dto.setFullName(student.getFullName());
            dto.setEnrollmentNo(student.getEnrollmentNo());
            dto.setContactNo(student.getContactNo());
            dto.setAddress(student.getAddress());

            // Assuming UserDTO mapping is handled, set user DTO here
            UserDTO userDTO = mapUserToUserDTO(student.getUser());
            dto.setUser(userDTO);

            return dto;
        }).collect(Collectors.toList());

        return studentDTOS;
    }

    private UserDTO mapUserToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());            // Adjust field names as per your User and UserDTO classes
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        // Map other relevant User fields to UserDTO fields here

        return userDTO;
    }




}
