package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Assessment;
import com.AssessAI.AssessAI.repository.AssessmentRepository;
import com.AssessAI.AssessAI.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public Assessment saveAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    @Override
    public Optional<Assessment> getAssessmentById(Long id) {
        return assessmentRepository.findById(id);
    }

    @Override
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    @Override
    public Assessment updateAssessment(Long id, Assessment updatedAssessment) {
        return assessmentRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedAssessment.getName());
                    existing.setDescription(updatedAssessment.getDescription());
                    existing.setDate(updatedAssessment.getDate());
                    existing.setClassroom(updatedAssessment.getClassroom());
                    return assessmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Assessment not found with id " + id));
    }

    @Override
    public void deleteAssessment(Long id) {
        if (!assessmentRepository.existsById(id)) {
            throw new RuntimeException("Assessment not found with id " + id);
        }
        assessmentRepository.deleteById(id);
    }
}
