package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Feedback;
import com.AssessAI.AssessAI.repository.FeedbackRepository;
import com.AssessAI.AssessAI.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        return feedbackRepository.findById(id)
                .map(existing -> {
                    existing.setComments(updatedFeedback.getComments());
                    existing.setFeedbackDate(updatedFeedback.getFeedbackDate());
                    existing.setTeacherAddonData(updatedFeedback.getTeacherAddonData());
                    existing.setStudent(updatedFeedback.getStudent());
                    existing.setTeacher(updatedFeedback.getTeacher());
                    existing.setClassroom(updatedFeedback.getClassroom());
                    return feedbackRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Feedback not found with id " + id));
    }

    @Override
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback not found with id " + id);
        }
        feedbackRepository.deleteById(id);
    }
}
