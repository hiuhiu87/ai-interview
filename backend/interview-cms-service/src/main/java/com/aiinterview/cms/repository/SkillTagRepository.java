package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.SkillTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillTagRepository extends JpaRepository<SkillTag, Integer> {
    Optional<SkillTag> findByName(String name);
}
