package com.aiinterview.cms.entity;

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
public class SkillTag {

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

    public SkillTag() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<QuestionTag> getQuestionTags() {
        return questionTags;
    }

    public void setQuestionTags(List<QuestionTag> questionTags) {
        this.questionTags = questionTags;
    }

    public List<TemplateSkillTag> getTemplateSkillTags() {
        return templateSkillTags;
    }

    public void setTemplateSkillTags(List<TemplateSkillTag> templateSkillTags) {
        this.templateSkillTags = templateSkillTags;
    }
}
