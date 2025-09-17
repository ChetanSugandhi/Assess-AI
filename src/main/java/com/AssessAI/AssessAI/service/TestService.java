package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Test;

import java.util.List;
import java.util.Optional;

public interface TestService {
    Test saveTest(Test test);
    Test updateTest(Long id, Test test);
    void deleteTest(Long id);
    Optional<Test> getTestById(Long id);
    List<Test> getAllTests();
}
