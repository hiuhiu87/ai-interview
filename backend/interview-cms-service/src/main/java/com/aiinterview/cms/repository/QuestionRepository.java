package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    @Override
    @EntityGraph(attributePaths = {"skill", "template", "questionTags", "questionTags.tag"})
    List<Question> findAll();
}
