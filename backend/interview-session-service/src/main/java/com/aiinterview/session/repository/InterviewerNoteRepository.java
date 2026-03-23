package com.aiinterview.session.repository;

import com.aiinterview.session.entity.InterviewerNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InterviewerNoteRepository extends JpaRepository<InterviewerNote, UUID> {
}
