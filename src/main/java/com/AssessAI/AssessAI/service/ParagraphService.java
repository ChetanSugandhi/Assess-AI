package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Paragraph;

import java.util.List;
import java.util.Optional;

public interface ParagraphService {
    Paragraph saveParagraph(Paragraph paragraph);
    Paragraph updateParagraph(Long id, Paragraph paragraph);
    void deleteParagraph(Long id);
    Optional<Paragraph> getParagraphById(Long id);
    List<Paragraph> getAllParagraphs();
    Paragraph getParagraphByQuestionId(Long questionId);
}
