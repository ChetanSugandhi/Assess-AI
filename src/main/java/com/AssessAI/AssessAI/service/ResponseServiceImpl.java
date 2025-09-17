package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Response;
import com.AssessAI.AssessAI.repository.ResponseRepository;
import com.AssessAI.AssessAI.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponseServiceImpl implements ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    @Override
    public Response saveResponse(Response response) {
        return responseRepository.save(response);
    }

    @Override
    public Response updateResponse(Long id, Response response) {
        Optional<Response> existing = responseRepository.findById(id);
        if(existing.isPresent()) {
            Response r = existing.get();
            r.setAnswer(response.getAnswer());
            r.setSubmittedAt(response.getSubmittedAt());
            r.setStudent(response.getStudent());
            r.setTeacher(response.getTeacher());
            r.setQuestion(response.getQuestion());
            return responseRepository.save(r);
        }
        return null; // ya exception throw karo
    }

    @Override
    public void deleteResponse(Long id) {
        responseRepository.deleteById(id);
    }

    @Override
    public Optional<Response> getResponseById(Long id) {
        return responseRepository.findById(id);
    }

    @Override
    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }

    @Override
    public List<Response> getResponsesByStudentId(Long studentId) {
        return responseRepository.findByStudentQId(studentId);
    }

    @Override
    public List<Response> getResponsesByQuestionId(Long questionId) {
        return responseRepository.findByQuestionQId(questionId);
    }
}
