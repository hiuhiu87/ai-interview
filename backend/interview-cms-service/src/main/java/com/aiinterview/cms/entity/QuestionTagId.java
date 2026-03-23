package com.aiinterview.cms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class QuestionTagId implements Serializable {

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    public QuestionTagId() {
    }

    public QuestionTagId(UUID questionId, Integer tagId) {
        this.questionId = questionId;
        this.tagId = tagId;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof QuestionTagId that)) {
            return false;
        }
        return Objects.equals(questionId, that.questionId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, tagId);
    }
}
