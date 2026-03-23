package com.aiinterview.cms.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "question_tags")
public class QuestionTag {

    @EmbeddedId
    private QuestionTagId id;

    @MapsId("questionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private SkillTag skillTag;

    public QuestionTag() {
    }

    public QuestionTagId getId() {
        return id;
    }

    public void setId(QuestionTagId id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
        if (question != null) {
            if (id == null) {
                id = new QuestionTagId();
            }
            id.setQuestionId(question.getId());
        }
    }

    public SkillTag getSkillTag() {
        return skillTag;
    }

    public void setSkillTag(SkillTag skillTag) {
        this.skillTag = skillTag;
        if (skillTag != null) {
            if (id == null) {
                id = new QuestionTagId();
            }
            id.setTagId(skillTag.getId());
        }
    }
}
