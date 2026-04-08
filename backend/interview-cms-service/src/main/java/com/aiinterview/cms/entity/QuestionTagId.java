package com.aiinterview.cms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class QuestionTagId implements Serializable {

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

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
