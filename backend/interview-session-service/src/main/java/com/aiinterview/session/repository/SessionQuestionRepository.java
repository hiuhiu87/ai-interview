package com.aiinterview.session.repository;

import com.aiinterview.session.entity.SessionQuestion;
import com.aiinterview.session.entity.SessionQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionQuestionRepository extends JpaRepository<SessionQuestion, SessionQuestionId> {
}
