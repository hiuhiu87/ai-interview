package com.aiinterview.cms.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "template_skill_tags")
public class TemplateSkillTag {

    @EmbeddedId
    private TemplateSkillTagId id;

    @MapsId("templateId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @MapsId("skillTagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_tag_id", nullable = false)
    private SkillTag skillTag;

    public TemplateSkillTag() {
    }

    public TemplateSkillTagId getId() {
        return id;
    }

    public void setId(TemplateSkillTagId id) {
        this.id = id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
        if (template != null) {
            if (id == null) {
                id = new TemplateSkillTagId();
            }
            id.setTemplateId(template.getId());
        }
    }

    public SkillTag getSkillTag() {
        return skillTag;
    }

    public void setSkillTag(SkillTag skillTag) {
        this.skillTag = skillTag;
        if (skillTag != null) {
            if (id == null) {
                id = new TemplateSkillTagId();
            }
            id.setSkillTagId(skillTag.getId());
        }
    }
}
