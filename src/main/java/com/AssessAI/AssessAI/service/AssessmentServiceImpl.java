package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assessment;
import com.AssessAI.AssessAI.models.Classroom;
import com.AssessAI.AssessAI.payloads.AssessmentDTO;
import com.AssessAI.AssessAI.repository.AssessmentRepository;
import com.AssessAI.AssessAI.repository.ClassroomRepository;
import com.AssessAI.AssessAI.service.AssessmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AssessmentDTO saveAssessment(AssessmentDTO assessmentDTO) {

        if(!classroomRepository.existsByClassroomCode(assessmentDTO.getClassroomCode())) {
            throw new IllegalArgumentException("Classroom not found with classroom code : " + assessmentDTO.getClassroomCode());
        }

        Assessment assessment = new Assessment();
        assessment.setVideoLink(assessmentDTO.getVideoLink());
        assessment.setVideoDescription(assessmentDTO.getVideoDescription());
        assessment.setAudioLink(assessmentDTO.getAudioLink());
        assessment.setAudioDescription(assessmentDTO.getAudioDescription());
        assessment.setTextLink(assessmentDTO.getTextLink());
        assessment.setTextDescription(assessmentDTO.getTextDescription());

        Classroom fetchClassroom = classroomRepository.findByClassroomCode(assessmentDTO.getClassroomCode()).get();
        assessment.setClassroom(fetchClassroom);

        Assessment savedAssessment = assessmentRepository.save(assessment);
        return modelMapper.map(savedAssessment, AssessmentDTO.class);
    }

    @Override
    public AssessmentDTO getAssessmentOfClass(String classroomCode) {
        if(!classroomRepository.existsByClassroomCode(classroomCode)) {
            throw new IllegalArgumentException("Classroom not found with classroom code : " + classroomCode);
        }

        Classroom fetchClassroom = classroomRepository.findByClassroomCode(classroomCode).get();

        Assessment fetchAssessment = fetchClassroom.getAssessment();

        return modelMapper.map(fetchAssessment, AssessmentDTO.class);
    }

    @Override
    @Transactional
    public String deleteAssessment(Long id) {
        if (!assessmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Assessment not found with id " + id);
        }
        assessmentRepository.deleteById(id);
        return "Successfully delete Assessment.";
    }

}
