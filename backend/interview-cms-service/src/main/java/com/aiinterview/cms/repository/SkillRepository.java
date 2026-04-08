package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    Optional<Skill> findByCode(String code);
}
