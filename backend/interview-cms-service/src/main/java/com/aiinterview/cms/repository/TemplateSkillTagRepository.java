package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.TemplateSkillTag;
import com.aiinterview.cms.entity.TemplateSkillTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateSkillTagRepository extends JpaRepository<TemplateSkillTag, TemplateSkillTagId> {
}
