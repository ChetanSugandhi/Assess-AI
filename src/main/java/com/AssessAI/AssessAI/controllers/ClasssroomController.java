package com.AssessAI.AssessAI.controllers;

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


    @DeleteMapping("/{classroomId}")
    public ResponseEntity<String> deleteClassroomById(@PathVariable Long classroomId) {
        String result = classroomService.deleteClassroom(classroomId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
