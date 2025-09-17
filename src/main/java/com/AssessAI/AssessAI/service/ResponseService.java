package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Response;

import java.util.List;
import java.util.Optional;

public interface ResponseService {
    Response saveResponse(Response response);
    Response updateResponse(Long id, Response response);
    void deleteResponse(Long id);
    Optional<Response> getResponseById(Long id);
    List<Response> getAllResponses();
    List<Response> getResponsesByStudentId(Long studentId);
    List<Response> getResponsesByQuestionId(Long questionId);
}
