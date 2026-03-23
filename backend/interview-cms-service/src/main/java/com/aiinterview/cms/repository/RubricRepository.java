package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.Rubric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RubricRepository extends JpaRepository<Rubric, UUID> {
    List<Rubric> findByQuestionId(UUID questionId);
}
