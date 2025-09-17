package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.MCQ;
import java.util.List;
import java.util.Optional;

public interface MCQService {
    MCQ saveMCQ(MCQ mcq);
    Optional<MCQ> getMCQById(Long id);
    List<MCQ> getAllMCQs();
    MCQ updateMCQ(Long id, MCQ updatedMCQ);
    void deleteMCQ(Long id);
}
