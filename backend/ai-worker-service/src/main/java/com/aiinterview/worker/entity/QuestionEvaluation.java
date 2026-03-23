package com.aiinterview.worker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "question_evaluations")
public class QuestionEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column(name = "ai_score")
    private Integer aiScore;

    @Column(name = "interviewer_score")
    private Integer interviewerScore;

    @Column(name = "ai_evidence", columnDefinition = "TEXT")
    private String aiEvidence;

    @Column(name = "ai_feedback", columnDefinition = "TEXT")
    private String aiFeedback;

    public QuestionEvaluation() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public Integer getAiScore() {
        return aiScore;
    }

    public void setAiScore(Integer aiScore) {
        this.aiScore = aiScore;
    }

    public Integer getInterviewerScore() {
        return interviewerScore;
    }

    public void setInterviewerScore(Integer interviewerScore) {
        this.interviewerScore = interviewerScore;
    }

    public String getAiEvidence() {
        return aiEvidence;
    }

    public void setAiEvidence(String aiEvidence) {
        this.aiEvidence = aiEvidence;
    }

    public String getAiFeedback() {
        return aiFeedback;
    }

    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }
}
