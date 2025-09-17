package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.MCQ;
import com.AssessAI.AssessAI.repository.MCQRepository;
import com.AssessAI.AssessAI.service.MCQService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MCQServiceImpl implements MCQService {

    private final MCQRepository mcqRepository;

    public MCQServiceImpl(MCQRepository mcqRepository) {
        this.mcqRepository = mcqRepository;
    }

    @Override
    public MCQ saveMCQ(MCQ mcq) {
        return mcqRepository.save(mcq);
    }

    @Override
    public Optional<MCQ> getMCQById(Long id) {
        return mcqRepository.findById(id);
    }

    @Override
    public List<MCQ> getAllMCQs() {
        return mcqRepository.findAll();
    }

    @Override
    public MCQ updateMCQ(Long id, MCQ updatedMCQ) {
        return mcqRepository.findById(id)
                .map(existing -> {
                    existing.setOptionA(updatedMCQ.getOptionA());
                    existing.setOptionB(updatedMCQ.getOptionB());
                    existing.setOptionC(updatedMCQ.getOptionC());
                    existing.setOptionD(updatedMCQ.getOptionD());
                    existing.setCorrectAnswer(updatedMCQ.getCorrectAnswer());
                    existing.setQuestion(updatedMCQ.getQuestion());
                    return mcqRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("MCQ not found with id " + id));
    }

    @Override
    public void deleteMCQ(Long id) {
        if (!mcqRepository.existsById(id)) {
            throw new RuntimeException("MCQ not found with id " + id);
        }
        mcqRepository.deleteById(id);
    }
}
