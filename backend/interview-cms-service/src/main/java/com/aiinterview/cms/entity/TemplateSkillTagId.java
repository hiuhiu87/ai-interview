package com.aiinterview.cms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TemplateSkillTagId implements Serializable {

    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    @Column(name = "skill_tag_id", nullable = false)
    private Integer skillTagId;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TemplateSkillTagId that)) {
            return false;
        }
        return Objects.equals(templateId, that.templateId) && Objects.equals(skillTagId, that.skillTagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateId, skillTagId);
    }
}
