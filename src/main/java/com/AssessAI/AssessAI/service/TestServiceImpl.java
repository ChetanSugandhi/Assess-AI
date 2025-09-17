package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Test;
import com.AssessAI.AssessAI.repository.TestRepository;
import com.AssessAI.AssessAI.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;

    @Override
    public Test saveTest(Test test) {
        return testRepository.save(test);
    }

    @Override
    public Test updateTest(Long id, Test test) {
        Optional<Test> existing = testRepository.findById(id);
        if(existing.isPresent()) {
            Test t = existing.get();
            t.setTestName(test.getTestName());
            t.setStartTime(test.getStartTime());
            t.setEndTime(test.getEndTime());
            t.setAssignment(test.getAssignment());
            t.setAssessment(test.getAssessment());
            // questions set usually managed separately
            return testRepository.save(t);
        }
        return null; // ya exception throw karo
    }

    @Override
    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }

    @Override
    public Optional<Test> getTestById(Long id) {
        return testRepository.findById(id);
    }

    @Override
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }
}
