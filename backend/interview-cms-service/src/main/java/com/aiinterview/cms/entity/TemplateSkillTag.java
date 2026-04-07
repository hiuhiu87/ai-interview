package com.aiinterview.cms.entity;

import com.aiinterview.common.entity.CommonEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "template_skill_tags", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueTemplateAndTag", columnNames = {"template_id", "skill_tag_id"})
})
public class TemplateSkillTag extends CommonEntity {

    @EmbeddedId
    private TemplateSkillTagId id;

    @MapsId("templateId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @MapsId("skillTagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_tag_id", nullable = false)
    private Tag tag;

}
