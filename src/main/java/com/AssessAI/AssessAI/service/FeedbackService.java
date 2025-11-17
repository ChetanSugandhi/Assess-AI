package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Feedback;
import com.AssessAI.AssessAI.payloads.QuestionAnswerDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FeedbackService {
    Feedback saveFeedback(Feedback feedback);
    Optional<Feedback> getFeedbackById(Long id);
    List<Feedback> getAllFeedbacks();
    Feedback updateFeedback(Long id, Feedback updatedFeedback);
    void deleteFeedback(Long id);

    String generateFeedback(List<QuestionAnswerDTO> qaList) throws Exception;

    Map<String, Object> parseFeedback(String aiText);
}
