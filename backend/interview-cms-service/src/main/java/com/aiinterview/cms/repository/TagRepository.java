package com.aiinterview.cms.repository;

import com.aiinterview.cms.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByName(String name);
    Optional<Tag> findByCode(String code);
}
