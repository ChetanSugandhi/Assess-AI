package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByStudentQId(Long studentId);
    List<Response> findByQuestionQId(Long questionId);
}
