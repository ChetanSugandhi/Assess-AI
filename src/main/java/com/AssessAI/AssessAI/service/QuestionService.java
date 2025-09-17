package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Question saveQuestion(Question question);
    Question updateQuestion(Long id, Question question);
    void deleteQuestion(Long id);
    Optional<Question> getQuestionById(Long id);
    List<Question> getAllQuestions();
    List<Question> getQuestionsByTestId(Long testId);
}
