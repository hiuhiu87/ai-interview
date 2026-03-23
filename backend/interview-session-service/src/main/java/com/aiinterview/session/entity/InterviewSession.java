package com.aiinterview.session.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "interview_sessions")
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "interviewer_id", nullable = false)
    private UUID interviewerId;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "ai_overall_summary", columnDefinition = "TEXT")
    private String aiOverallSummary;

    @Column(name = "final_ai_score")
    private Double finalAiScore;

    @Column(name = "final_interviewer_score")
    private Double finalInterviewerScore;

    @Column(name = "cv_url")
    private String cvUrl;

    @Column(name = "applied_position")
    private String appliedPosition;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewerNote> notes = new ArrayList<>();

    public InterviewSession() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(UUID interviewerId) {
        this.interviewerId = interviewerId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public String getAiOverallSummary() {
        return aiOverallSummary;
    }

    public void setAiOverallSummary(String aiOverallSummary) {
        this.aiOverallSummary = aiOverallSummary;
    }

    public Double getFinalAiScore() {
        return finalAiScore;
    }

    public void setFinalAiScore(Double finalAiScore) {
        this.finalAiScore = finalAiScore;
    }

    public Double getFinalInterviewerScore() {
        return finalInterviewerScore;
    }

    public void setFinalInterviewerScore(Double finalInterviewerScore) {
        this.finalInterviewerScore = finalInterviewerScore;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getAppliedPosition() {
        return appliedPosition;
    }

    public void setAppliedPosition(String appliedPosition) {
        this.appliedPosition = appliedPosition;
    }

    public List<SessionQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SessionQuestion> questions) {
        this.questions = questions;
    }

    public List<InterviewerNote> getNotes() {
        return notes;
    }

    public void setNotes(List<InterviewerNote> notes) {
        this.notes = notes;
    }

    public void addNote(InterviewerNote note) {
        notes.add(note);
        note.setSession(this);
    }

    public void addQuestion(SessionQuestion question) {
        questions.add(question);
        question.setSession(this);
    }
}
