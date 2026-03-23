package com.aiinterview.cms.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "expected_answer", columnDefinition = "TEXT")
    private String expectedAnswer;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "keywords", columnDefinition = "jsonb")
    private List<String> keywords = new ArrayList<>();

    @Column(name = "level")
    private String level;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rubric> rubrics = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionTag> questionTags = new ArrayList<>();

    public Question() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    public void setExpectedAnswer(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Rubric> getRubrics() {
        return rubrics;
    }

    public void setRubrics(List<Rubric> rubrics) {
        this.rubrics = rubrics;
    }

    public List<QuestionTag> getQuestionTags() {
        return questionTags;
    }

    public void setQuestionTags(List<QuestionTag> questionTags) {
        this.questionTags = questionTags;
    }

    public void addRubric(Rubric rubric) {
        rubrics.add(rubric);
        rubric.setQuestion(this);
    }

    public void removeRubric(Rubric rubric) {
        rubrics.remove(rubric);
        rubric.setQuestion(null);
    }

    public void addQuestionTag(QuestionTag questionTag) {
        questionTags.add(questionTag);
        questionTag.setQuestion(this);
    }
}
