package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Question;
import com.AssessAI.AssessAI.repository.QuestionRepository;
import com.AssessAI.AssessAI.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Long id, Question question) {
        Optional<Question> existing = questionRepository.findById(id);
        if(existing.isPresent()) {
            Question q = existing.get();
            q.setText(question.getText());
            q.setMarks(question.getMarks());
            q.setTest(question.getTest());
            // MCQ or Paragraph update usually handled separately
            return questionRepository.save(q);
        }
        return null; // ya exception throw karo
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public List<Question> getQuestionsByTestId(Long testId) {
        return questionRepository.findByTestQId(testId);
    }
}
