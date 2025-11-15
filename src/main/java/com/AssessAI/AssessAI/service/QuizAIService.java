package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.*;
import com.AssessAI.AssessAI.repository.*;
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

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Transactional
    public String generateAndReturnQuizJson(String title, String description, String difficulty, String classroomCode, Long assignmentId, int numMcqs, int numWriting) throws Exception {
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

        Assignment findAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id : " + assignmentId));


        // Save test
        Test test = new Test();
        test.setTestName(title);
        test.setAssignment(findAssignment);

        testRepo.save(test);

        for (Map<String, Object> item : questions) {
            Question question = new Question();
            question.setTest(test);
            question.setText((String) item.get("question"));
            question.setAssignment(findAssignment);

            String rawType = (String) item.get("type");
            String type = rawType.toLowerCase();
            question = questionRepo.save(question);

            if (type.contains("mcq") || type.contains("multiple")) {
                MCQ mcq = new MCQ();
                Map<String, String> optionsMap = (Map<String, String>) item.get("options");
                mcq.setOptionA(optionsMap.get("A"));
                mcq.setOptionB(optionsMap.get("B"));
                mcq.setOptionC(optionsMap.get("C"));
                mcq.setOptionD(optionsMap.get("D"));
                mcq.setCorrectAnswer((String) item.get("correct_answer"));

                mcq.setQuestion(question);

//                mcqRepo.save(mcq);

                question.setMcq(mcq);
            } else if (type.contains("paragraph") || type.contains("paragraph-based")) {
                Paragraph para = new Paragraph();
                para.setPassage("Write answer here.");
                para.setQuestion(question);
                paraRepo.save(para);

                question.setPara(para);
//                questionRepo.save(question);
            }
                questionRepo.save(question);

        }

        return aiText;
    }

}
