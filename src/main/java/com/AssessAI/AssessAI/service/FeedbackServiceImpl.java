package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Feedback;
import com.AssessAI.AssessAI.payloads.QuestionAnswerDTO;
import com.AssessAI.AssessAI.repository.FeedbackRepository;
import com.AssessAI.AssessAI.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
            You are an academic evaluator.

            You will receive a JSON list that contains: 
            - a set of questions 
            - the student's answers.

            Your job is to evaluate the student's *overall performance* based only on the combined impression of all answers.
            DO NOT evaluate each question individually.

            -----------------------------------------------------
            STRICT OUTPUT FORMAT (DO NOT BREAK THIS STRUCTURE):
            -----------------------------------------------------

            1. **Key Strengths (4 bullet points only):**
               - Highlight four major strengths observed across all answers.
               - Keep each bullet crisp, specific, and academic in tone.

            2. **Key Improvements (4 bullet points only):**
               - Mention four areas where the student needs improvement.
               - Make each point constructive, not harsh.

            3. **Detailed Feedback (150-200 words):**
               - Provide a holistic, academic-style feedback paragraph.
               - Discuss clarity of thought, depth of understanding, consistency, reasoning ability, accuracy, and writing quality.
               - Provide an expert-level, well-structured evaluation that feels like a teacher’s assessment.
               - Avoid repeating bullet points verbatim.

            -----------------------------------------------------
            Additional Rules:
            -----------------------------------------------------
            - Maintain a formal, evaluator-style tone.
            - Ensure readability and clean structure.
            - Base the evaluation ONLY on the combined quality of all answers.
            - DO NOT mention the JSON or reference question numbers.

            -----------------------------------------------------
            JSON Input:
            -----------------------------------------------------
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

    @Override
    public Map<String, Object> parseFeedback(String aiText) {

        Map<String, Object> map = new HashMap<>();

        // Extract strengths
        List<String> strengths = new ArrayList<>();
        if (aiText.contains("Key Strengths")) {
            String part = aiText.split("Key Improvements")[0];
            String[] lines = part.split("-");
            for (int i = 1; i < lines.length; i++) {
                strengths.add(lines[i].trim());
            }
        }

        // Extract improvements
        List<String> improvements = new ArrayList<>();
        if (aiText.contains("Key Improvements")) {
            String part = aiText.split("Detailed Feedback")[0];
            String[] lines = part.split("-");
            for (int i = 1; i < lines.length; i++) {
                improvements.add(lines[i].trim());
            }
        }

        // Extract detailed feedback
        String detailed = "";
        if (aiText.contains("Detailed Feedback")) {
            detailed = aiText.substring(aiText.indexOf("Detailed Feedback")).replace("Detailed Feedback (100–150 words):", "").trim();
        }

        map.put("strengths", strengths);
        map.put("improvements", improvements);
        map.put("detailedFeedback", detailed);

        return map;
    }


}
