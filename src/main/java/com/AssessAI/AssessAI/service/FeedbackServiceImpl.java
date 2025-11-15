package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Feedback;
import com.AssessAI.AssessAI.payloads.QuestionAnswerDTO;
import com.AssessAI.AssessAI.repository.FeedbackRepository;
import com.AssessAI.AssessAI.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    private final Client client = new Client();
    private final ObjectMapper mapper = new ObjectMapper();

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

    @Override
    public String generateFeedback(List<QuestionAnswerDTO> qaList) throws Exception {

        // Convert question-answer list into JSON String
        String jsonInput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(qaList);

        String prompt = """
                You are an educational evaluator.
                
                You will receive a JSON list containing questions and the student's answers.
                Your task is to evaluate the student's overall performance based on all answers combined.
                
                Do NOT evaluate answers individually.
                Instead, provide a holistic and well-organized assessment covering:
                - General Answer Quality
                - Conceptual Understanding
                - Accuracy and Knowledge Depth
                - Writing & Grammar Skills
                - Logical Reasoning Ability
                - Consistency Across Answers
                
                ### OUTPUT FORMAT (STRICT REQUIREMENT):
                
                1. **OverallFeedback (minimum 150 words):**
                   Provide a clear, structured, and readable evaluation summarizing the student's overall performance.\s
                   Discuss clarity of thought, depth of understanding, accuracy, writing quality, strengths, weaknesses, and overall learning level.
                   The tone must be formal, academic, and constructive.
                
                2. **OverallStrengths (approx. 50 words):**
                   Provide a concise list-style summary highlighting the strongest aspects of the student’s performance.
                
                3. **OverallWeaknesses (approx. 50 words):**
                   Provide a concise list-style summary of the key weaknesses and areas needing improvement.
                
                Ensure the overall output is:
                - Formal and evaluator-style \s
                - Easy to read and well-structured \s
                - Helpful for academic development \s
                - Based only on the combined impression of all answers, not per-question evaluation
                
                ### JSON Input:                
                %s
                
                """.formatted(jsonInput);

        int attempts = 0;
        Exception lastException = null;

        while (attempts < 3) {
            try {
                GenerateContentResponse response =
                        client.models.generateContent(
                                "gemini-2.5-flash",
                                prompt,
                                null
                        );

                return response.text();

            } catch (Exception ex) {
                lastException = ex;
                attempts++;
                Thread.sleep(1000 * attempts);
            }
        }

        throw lastException;
    }
}
