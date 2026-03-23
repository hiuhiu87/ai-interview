package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.QuestionTag;
import com.aiinterview.cms.entity.QuestionTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, QuestionTagId> {
}
