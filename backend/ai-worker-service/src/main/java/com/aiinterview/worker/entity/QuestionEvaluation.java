package com.aiinterview.worker.entity;

import com.aiinterview.common.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "question_evaluations", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueSessionAndQuestion", columnNames = {"session_id", "question_id"})
})
public class QuestionEvaluation extends CommonEntity {

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

}
