package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.service.QuizAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizAIService quizAIService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAndReturnQuiz(@RequestParam String title,
                                                        @RequestParam String description,
                                                        @RequestParam String difficulty,
                                                        @RequestParam String classroomCode,
                                                        @RequestParam Long assignmentId,
                                                        @RequestParam int numMcqs,
                                                        @RequestParam int numWriting) {
        try {
            String quizJson = quizAIService.generateAndReturnQuizJson(title, description, difficulty, classroomCode, assignmentId, numMcqs, numWriting);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(quizJson);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
