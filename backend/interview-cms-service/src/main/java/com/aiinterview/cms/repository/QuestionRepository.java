package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    @Override
    @EntityGraph(attributePaths = {"skill", "template", "questionTags", "questionTags.tag"})
    List<Question> findAll();

    @Override
    @EntityGraph(attributePaths = {"skill", "template", "questionTags", "questionTags.tag"})
    Optional<Question> findById(UUID id);

    @EntityGraph(attributePaths = {"skill", "template", "questionTags", "questionTags.tag"})
    @Query("""
            select distinct q
            from Question q
            join q.questionTags questionTag
            where questionTag.tag.id in :tagIds
            """)
    List<Question> findDistinctByTagIds(Collection<Integer> tagIds);
}
