package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.*;
import com.AssessAI.AssessAI.payloads.ParagraphResult;
import com.AssessAI.AssessAI.payloads.QuestionResultDTO;
import com.AssessAI.AssessAI.payloads.StudentAnswerDTO;
import com.AssessAI.AssessAI.payloads.TestResultResponseDTO;
import com.AssessAI.AssessAI.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService{

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private TestRepository testRepo;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ResponseRepository responseRepository;

    private final Client client = new Client();

    @Override
    public TestResultResponseDTO evaluateTest(Long testId, Long studentId, List<StudentAnswerDTO> answers) throws Exception {

        Test test = testRepo.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id : " + testId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("student not found with id : "+ studentId));

        int totalMarks = 0;
        int obtained = 0;

        List<QuestionResultDTO> questionResultDTOS = new ArrayList<>();

        for (StudentAnswerDTO studentAnswerDTO : answers) {
            Long quesId = studentAnswerDTO.getQuestionId();
            Question q = questionRepo.findById(quesId)
                    .orElseThrow(() -> new IllegalArgumentException("Question not found with id : " + quesId));

            QuestionResultDTO resultDTO = new QuestionResultDTO();
            resultDTO.setQuestionId(quesId);
            resultDTO.setQuestionText(q.getText());

            if (q.getMcq() != null) {
                resultDTO.setType("MCQ");

                MCQ mcq = q.getMcq();

                String studentText = studentAnswerDTO.getAnswer();
                if (studentText == null) studentText = "";
                studentText = studentText.trim();

                // Get stored correct option letter (A/B/C/D)
                String correctLetter = mcq.getCorrectAnswer();
                if (correctLetter == null) correctLetter = "";

                // Convert A/B/C/D to text
                String correctText = switch (correctLetter.toUpperCase()) {
                    case "A" -> mcq.getOptionA();
                    case "B" -> mcq.getOptionB();
                    case "C" -> mcq.getOptionC();
                    case "D" -> mcq.getOptionD();
                    default -> "";
                };

                resultDTO.setStudentAnswer(studentText);
                resultDTO.setCorrectAnswer(correctText);

                totalMarks++;

                // Compare full text
                boolean check = studentText.equalsIgnoreCase(correctText.trim());
                resultDTO.setCorrect(check);

                if (check) {
                    obtained++;
                }
            }
            // paragraph
            else if(q.getPara() != null) {
                resultDTO.setType("Paragraph");
                resultDTO.setStudentParagraph(studentAnswerDTO.getAnswer());

                totalMarks += 2;

                ParagraphResult paragraphResult = evaluateParagraph(q.getText(), studentAnswerDTO.getAnswer());

                int sc = paragraphResult.getScore();
                resultDTO.setParagraphScore(sc);

                if (sc == 2) {
                    resultDTO.setCorrectnessLevel("FULL");
                } else if (sc == 1) {
                    resultDTO.setCorrectnessLevel("PARTIAL");
                } else {
                    resultDTO.setCorrectnessLevel("WRONG");
                }

                resultDTO.setParagraphEvaluation(paragraphResult.getEvaluation());

                obtained += paragraphResult.getScore();
            }

            questionResultDTOS.add(resultDTO);

            Response response = new Response();
            response.setStudent(student);
            response.setTest(test);
            response.setQuestion(q);
            response.setAnswer(studentAnswerDTO.getAnswer());
            response.setAssignment(q.getAssignment());

            if(q.getMcq() != null) {
                response.setIsCorrect(resultDTO.isCorrect());
            }

            if(q.getPara() != null) {
                response.setParagraphScore(resultDTO.getParagraphScore());
                response.setParagraphEvaluation(resultDTO.getParagraphEvaluation());
            }

            responseRepository.save(response);

        }

        Result result = new Result();
        result.setStudent(student);
        result.setTestId(testId);
        result.setTotalScore(totalMarks);
        result.setMaxScore(obtained);

        resultRepository.save(result);

        TestResultResponseDTO responseDTO = new TestResultResponseDTO();
        responseDTO.setMaxScore(obtained);
        responseDTO.setTotalScore(totalMarks);
        responseDTO.setQuestionResults(questionResultDTOS);



        return responseDTO;
    }
    public ParagraphResult evaluateParagraph(String question, String answer) throws Exception {

        String prompt = """
            Evaluate the student's answer.
            Question: %s
            Answer: %s
            Give only JSON: {"score":0-2, "evaluation":"text"}
            """.formatted(question, answer);

        int attempts = 0;
        while (attempts < 3) {
            try {
                GenerateContentResponse res =
                        client.models.generateContent("gemini-2.5-flash", prompt, null);

                String clean = res.text()
                        .replace("```json","")
                        .replace("```","")
                        .replace("`","")
                        .trim();

                return new ObjectMapper().readValue(clean, ParagraphResult.class);

            } catch (Exception ex) {
                attempts++;
                if (attempts == 3)
                    throw ex;  // final failure

                Thread.sleep(1000 * attempts); // backoff 1s → 2s → 3s
            }
        }

        return null; // unreachable
    }


}
