package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Result;
import com.AssessAI.AssessAI.repository.ResultRepository;
import com.AssessAI.AssessAI.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }

    @Override
    public Result updateResult(Long id, Result result) {
        Optional<Result> existing = resultRepository.findById(id);
        if(existing.isPresent()) {
            Result r = existing.get();
            r.setMarksObtained(result.getMarksObtained());
            r.setGrade(result.getGrade());
            r.setStudent(result.getStudent());
            r.setClassroom(result.getClassroom());
            r.setAssignment(result.getAssignment());
            r.setAssessment(result.getAssessment());
            return resultRepository.save(r);
        }
        return null; // ya exception throw karo
    }

    @Override
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }

    @Override
    public Optional<Result> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    @Override
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    @Override
    public List<Result> getResultsByStudentId(Long studentId) {
        return resultRepository.findByStudentId(studentId);
    }

    @Override
    public List<Result> getResultsByClassroomId(Long classroomId) {
        return resultRepository.findByClassroomId(classroomId);
    }

    @Override
    public List<Result> getResultsByAssignmentId(Long assignmentId) {
        return resultRepository.findByAssignmentId(assignmentId);
    }

    @Override
    public List<Result> getResultsByAssessmentId(Long assessmentId) {
        return resultRepository.findByAssessmentId(assessmentId);
    }
}
