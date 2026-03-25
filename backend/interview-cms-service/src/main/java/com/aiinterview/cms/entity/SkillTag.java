package com.aiinterview.cms.entity;

import com.aiinterview.common.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skill_tags")
public class SkillTag extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "category")
    private String category;

    @OneToMany(mappedBy = "skillTag")
    private List<QuestionTag> questionTags = new ArrayList<>();

    @OneToMany(mappedBy = "skillTag")
    private List<TemplateSkillTag> templateSkillTags = new ArrayList<>();

}
