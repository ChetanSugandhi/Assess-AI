package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Paragraph;
import com.AssessAI.AssessAI.repository.ParagraphRepository;
import com.AssessAI.AssessAI.service.ParagraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParagraphServiceImpl implements ParagraphService {

    @Autowired
    private ParagraphRepository paragraphRepository;

    @Override
    public Paragraph saveParagraph(Paragraph paragraph) {
        return paragraphRepository.save(paragraph);
    }

    @Override
    public Paragraph updateParagraph(Long id, Paragraph paragraph) {
        Optional<Paragraph> existing = paragraphRepository.findById(id);
        if(existing.isPresent()) {
            Paragraph p = existing.get();
            p.setPassage(paragraph.getPassage());
            p.setQuestion(paragraph.getQuestion());
            return paragraphRepository.save(p);
        }
        return null; // ya exception throw karo
    }

    @Override
    public void deleteParagraph(Long id) {
        paragraphRepository.deleteById(id);
    }

    @Override
    public Optional<Paragraph> getParagraphById(Long id) {
        return paragraphRepository.findById(id);
    }

    @Override
    public List<Paragraph> getAllParagraphs() {
        return paragraphRepository.findAll();
    }

    @Override
    public Paragraph getParagraphByQuestionId(Long questionId) {
        return paragraphRepository.findByQuestionId(questionId);
    }
}
