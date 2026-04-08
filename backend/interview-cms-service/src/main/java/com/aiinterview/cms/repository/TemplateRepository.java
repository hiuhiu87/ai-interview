package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {
    Optional<Template> findByCode(String code);

    @Override
    @EntityGraph(attributePaths = {"skill", "templateSkillTags", "templateSkillTags.tag"})
    List<Template> findAll();
}
