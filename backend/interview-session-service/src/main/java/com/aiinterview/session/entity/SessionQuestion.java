package com.aiinterview.session.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "session_questions")
public class SessionQuestion {

    @EmbeddedId
    private SessionQuestionId id;

    @MapsId("sessionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    public SessionQuestion() {
    }

    public SessionQuestionId getId() {
        return id;
    }

    public void setId(SessionQuestionId id) {
        this.id = id;
    }

    public InterviewSession getSession() {
        return session;
    }

    public void setSession(InterviewSession session) {
        this.session = session;
        if (session != null) {
            if (id == null) {
                id = new SessionQuestionId();
            }
            id.setSessionId(session.getId());
        }
    }

    public UUID getQuestionId() {
        return id != null ? id.getQuestionId() : null;
    }

    public void setQuestionId(UUID questionId) {
        if (id == null) {
            id = new SessionQuestionId();
        }
        id.setQuestionId(questionId);
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
}
