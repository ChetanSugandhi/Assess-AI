package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    // Agar custom queries chahiye toh yaha add kar sakte ho
}
