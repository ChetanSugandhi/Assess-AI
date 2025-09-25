package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.payloads.AssignmentDTO;
import com.AssessAI.AssessAI.payloads.ClassroomDTO;
import com.AssessAI.AssessAI.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classroom")
public class ClasssroomController {

    @Autowired
    private ClassroomService classroomService;

    // create new classroom
    @PostMapping("/create")
    public ResponseEntity<ClassroomDTO> savedClassroom(@RequestBody ClassroomDTO classroomDTO) {
        ClassroomDTO savedClassroom = classroomService.saveClassroom(classroomDTO);
        return new ResponseEntity<ClassroomDTO>(savedClassroom, HttpStatus.CREATED);
    }


    // show all classrooms
    @GetMapping("/allClassrooms")
    public ResponseEntity<List<ClassroomDTO>> getAllClassrooms() {
        List<ClassroomDTO> allClassrooms = classroomService.getAllClassrooms();
        return new ResponseEntity<List<ClassroomDTO>>(allClassrooms, HttpStatus.OK);
    }


// fetch the classrooms by classroom id
    @GetMapping("/{classroomId}")
    public ResponseEntity<ClassroomDTO> getClassroomById(@PathVariable Long classroomId) {
        ClassroomDTO classroomDTO = classroomService.getClassroomById(classroomId);
        return new ResponseEntity<ClassroomDTO>(classroomDTO, HttpStatus.OK);
    }


    // update the classroom
    @PutMapping("/update/{classroomId}")
    public ResponseEntity<ClassroomDTO> updateClassroom(@PathVariable Long classroomId,
                                                        @RequestBody ClassroomDTO classroomDTO) {
        ClassroomDTO updatedClassrom = classroomService.updateClassroom(classroomId, classroomDTO);
        return new ResponseEntity<ClassroomDTO>(updatedClassrom, HttpStatus.OK);
    }


    // delete classroom by id
    @DeleteMapping("/{classroomId}")
    public ResponseEntity<String> deleteClassroomById(@PathVariable Long classroomId) {
        String result = classroomService.deleteClassroom(classroomId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // teacher specific classrooms
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ClassroomDTO>> getAllClassroomOfTeacher(@PathVariable Long teacherId) {
        List<ClassroomDTO> classroomDTOS = classroomService.getAllClassroomsOfTeacherByTeacherId(teacherId);
        return new ResponseEntity<List<ClassroomDTO>>(classroomDTOS, HttpStatus.FOUND);
    }



    // student specific classrooms
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ClassroomDTO>> getAllClassroomOfStudent(@PathVariable Long studentId) {
        List<ClassroomDTO> classroomDTOS = classroomService.getAllClassroomsOfStudentByStudentId(studentId);
        return new ResponseEntity<List<ClassroomDTO>>(classroomDTOS, HttpStatus.FOUND);
    }


    // join classroom with classroom code
    @PostMapping("/{classroomCode}/student/join")
    public ResponseEntity<String> joinClassroomByClassroomCode(@PathVariable String classroomCode) {
        String result = classroomService.joinClassroomByClassroomCode(classroomCode);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }


    // remove student from classroom
    @DeleteMapping("/{classroomId}/student/{studentId}/remove")
    public ResponseEntity<String> removeStudentFromClassroom(@PathVariable Long classroomId,
                                                             @PathVariable Long studentId) {
        String result = classroomService.removeStudentFromClassroom(classroomId, studentId);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }


    // fetch the all assignments of a classroom with classroom code
    @GetMapping("/{classroomCode}/assignment")
    public ResponseEntity<List<AssignmentDTO>> getAllAssignmentOfClassroom(@PathVariable String classroomCode) {
        List<AssignmentDTO> assignmentDTOS = classroomService.getAllAssignmentOfClassroom(classroomCode);
        return new ResponseEntity<List<AssignmentDTO>>(assignmentDTOS, HttpStatus.OK);
    }

}
