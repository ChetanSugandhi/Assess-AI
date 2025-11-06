package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.MCQ;
import com.AssessAI.AssessAI.models.Paragraph;
import com.AssessAI.AssessAI.models.Question;
import com.AssessAI.AssessAI.models.Test;
import com.AssessAI.AssessAI.repository.MCQRepository;
import com.AssessAI.AssessAI.repository.ParagraphRepository;
import com.AssessAI.AssessAI.repository.QuestionRepository;
import com.AssessAI.AssessAI.repository.TestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class QuizAIService {

    @Autowired
    private TestRepository testRepo;
    @Autowired
    private QuestionRepository questionRepo;
    @Autowired
    private MCQRepository mcqRepo;
    @Autowired
    private ParagraphRepository paraRepo;

    @Transactional
    public String generateAndReturnQuizJson(String title, String description, String difficulty, int numMcqs, int numWriting) throws Exception {
        String prompt = String.format("Generate a quiz with %d questions based on:\n"
                + "Title: %s\nDescription: %s\nDifficulty: %s\n"
                + "- %d multiple-choice questions (MCQs), each with:\n"
                + "  - A clear question\n  - 4 answer options (A, B, C, D)\n  - The correct answer (specify A, B, C, or D)\n"
                + "- %d paragraph-based writing questions, each with:\n"
                + "  - A clear question requiring a short paragraph response\n"
                + "Output only a JSON array without explanations.", numMcqs + numWriting, title, description, difficulty, numMcqs, numWriting);

        com.google.genai.Client client = new com.google.genai.Client();
        com.google.genai.types.GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                null);

        String aiText = response.text();
        aiText = aiText.replaceAll("(?s)``````|`|json", "").trim();

        // Parsing here optional - if you want to save, do below (not mandatory for return)
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> questions = mapper.readValue(aiText, List.class);

        // Save test
        Test test = new Test();
        test.setTestName(title);
        testRepo.save(test);

        for (Map<String, Object> item : questions) {
            Question question = new Question();
            question.setTest(test);
            question.setText((String) item.get("question"));
            questionRepo.save(question);

            String type = (String) item.get("type");
            if ("multiple-choice".equals(type)) {
                MCQ mcq = new MCQ();
                Map<String, String> options = (Map<String, String>) item.get("options");
                mcq.setOptionA(options.get("A"));
                mcq.setOptionB(options.get("B"));
                mcq.setOptionC(options.get("C"));
                mcq.setOptionD(options.get("D"));
                mcq.setCorrectAnswer((String) item.get("correct_answer"));
                mcq.setQuestion(question);
                mcqRepo.save(mcq);
                question.setMcq(mcq);
                questionRepo.save(question);
            } else if ("paragraph".equals(type)) {
                Paragraph para = new Paragraph();
                para.setPassage("Write answer here.");
                para.setQuestion(question);
                paraRepo.save(para);
                question.setPara(para);
                questionRepo.save(question);
            }
        }

        return aiText;
    }

}
