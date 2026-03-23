package com.aiinterview.worker.repository;

import com.aiinterview.worker.entity.QuestionEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionEvaluationRepository extends JpaRepository<QuestionEvaluation, UUID> {
}
