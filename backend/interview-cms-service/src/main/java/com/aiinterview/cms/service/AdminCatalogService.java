package com.aiinterview.cms.service;

import com.aiinterview.cms.dto.admin.MetadataResponse;
import com.aiinterview.cms.dto.admin.QuestionRequest;
import com.aiinterview.cms.dto.admin.QuestionResponse;
import com.aiinterview.cms.dto.admin.SkillRequest;
import com.aiinterview.cms.dto.admin.SkillResponse;
import com.aiinterview.cms.dto.admin.TagRequest;
import com.aiinterview.cms.dto.admin.TagResponse;
import com.aiinterview.cms.dto.admin.TemplateRequest;
import com.aiinterview.cms.dto.admin.TemplateResponse;
import com.aiinterview.cms.entity.Question;
import com.aiinterview.cms.entity.QuestionTag;
import com.aiinterview.cms.entity.QuestionTagId;
import com.aiinterview.cms.entity.Skill;
import com.aiinterview.cms.entity.Tag;
import com.aiinterview.cms.entity.Template;
import com.aiinterview.cms.entity.TemplateSkillTag;
import com.aiinterview.cms.entity.TemplateSkillTagId;
import com.aiinterview.cms.repository.QuestionRepository;
import com.aiinterview.cms.repository.SkillRepository;
import com.aiinterview.cms.repository.TagRepository;
import com.aiinterview.cms.repository.TemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AdminCatalogService {

    private final SkillRepository skillRepository;
    private final TagRepository tagRepository;
    private final TemplateRepository templateRepository;
    private final QuestionBuilderService questionBuilderService;

    public AdminCatalogService(
            SkillRepository skillRepository,
            TagRepository tagRepository,
            TemplateRepository templateRepository,
            QuestionBuilderService questionBuilderService
    ) {
        this.skillRepository = skillRepository;
        this.tagRepository = tagRepository;
        this.templateRepository = templateRepository;
        this.questionBuilderService = questionBuilderService;
    }

    public MetadataResponse getMetadata() {
        return new MetadataResponse(
                questionBuilderService.getSupportedDifficulties(),
                questionBuilderService.getSupportedLevels(),
                getSkills(),
                getTags(),
                getTemplates()
        );
    }

    public List<SkillResponse> getSkills() {
        List<Skill> skills = skillRepository.findAll()
                .stream()
                .sorted(Comparator
                        .comparing((Skill skill) -> skill.getDisplayOrder() == null ? Integer.MAX_VALUE : skill.getDisplayOrder())
                        .thenComparing(Skill::getName))
                .toList();

        Map<Integer, SkillResponse> nodes = new LinkedHashMap<>();
        Map<Integer, List<SkillResponse>> children = new LinkedHashMap<>();

        for (Skill skill : skills) {
            children.put(skill.getId(), new ArrayList<>());
        }

        for (Skill skill : skills) {
            nodes.put(skill.getId(), new SkillResponse(
                    skill.getId(),
                    skill.getName(),
                    skill.getCode(),
                    skill.getDescription(),
                    skill.getDisplayOrder(),
                    skill.getParent() != null ? skill.getParent().getId() : null,
                    children.get(skill.getId())
            ));
        }

        List<SkillResponse> roots = new ArrayList<>();
        for (Skill skill : skills) {
            SkillResponse response = nodes.get(skill.getId());
            if (skill.getParent() == null) {
                roots.add(response);
                continue;
            }
            children.computeIfAbsent(skill.getParent().getId(), key -> new ArrayList<>()).add(response);
        }
        return roots;
    }

    public SkillResponse createSkill(SkillRequest request) {
        Skill skill = new Skill();
        applySkill(skill, request);
        return toSkillResponse(skillRepository.save(skill));
    }

    public SkillResponse updateSkill(Integer id, SkillRequest request) {
        Skill skill = findSkill(id);
        applySkill(skill, request);
        return toSkillResponse(skillRepository.save(skill));
    }

    public void deleteSkill(Integer id) {
        skillRepository.delete(findSkill(id));
    }

    public List<TagResponse> getTags() {
        return tagRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Tag::getCategory, Comparator.nullsLast(String::compareTo))
                        .thenComparing(Tag::getName))
                .map(this::toTagResponse)
                .toList();
    }

    public TagResponse createTag(TagRequest request) {
        Tag tag = new Tag();
        applyTag(tag, request);
        return toTagResponse(tagRepository.save(tag));
    }

    public TagResponse updateTag(Integer id, TagRequest request) {
        Tag tag = findTag(id);
        applyTag(tag, request);
        return toTagResponse(tagRepository.save(tag));
    }

    public void deleteTag(Integer id) {
        tagRepository.delete(findTag(id));
    }

    public List<TemplateResponse> getTemplates() {
        return templateRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Template::getName))
                .map(this::toTemplateResponse)
                .toList();
    }

    public TemplateResponse createTemplate(TemplateRequest request) {
        Template template = new Template();
        applyTemplate(template, request);
        return toTemplateResponse(templateRepository.save(template));
    }

    public TemplateResponse updateTemplate(Integer id, TemplateRequest request) {
        Template template = findTemplate(id);
        applyTemplate(template, request);
        return toTemplateResponse(templateRepository.save(template));
    }

    public void deleteTemplate(Integer id) {
        templateRepository.delete(findTemplate(id));
    }

    public List<QuestionResponse> getQuestions() {
        return questionBuilderService.getQuestions();
    }

    public QuestionResponse createQuestion(QuestionRequest request) {
        return questionBuilderService.createQuestion(request);
    }

    public QuestionResponse updateQuestion(UUID id, QuestionRequest request) {
        return questionBuilderService.updateQuestion(id, request);
    }

    public void deleteQuestion(UUID id) {
        questionBuilderService.deleteQuestion(id);
    }

    private void applySkill(Skill skill, SkillRequest request) {
        skill.setName(request.name().trim());
        skill.setCode(request.code().trim().toUpperCase());
        skill.setDescription(request.description());
        skill.setDisplayOrder(request.displayOrder());
        skill.setParent(request.parentId() == null ? null : findSkill(request.parentId()));
    }

    private void applyTag(Tag tag, TagRequest request) {
        tag.setName(request.name().trim());
        tag.setCode(request.code().trim().toUpperCase());
        tag.setCategory(request.category());
        tag.setDescription(request.description());
    }

    private void applyTemplate(Template template, TemplateRequest request) {
        template.setName(request.name().trim());
        template.setCode(request.code().trim().toUpperCase());
        template.setDescription(request.description());
        template.setDefaultDifficulty(request.defaultDifficulty());
        template.setSkill(request.skillId() == null ? null : findSkill(request.skillId()));

        template.getTemplateSkillTags().clear();
        if (request.tagIds() == null) {
            return;
        }

        for (Integer tagId : request.tagIds()) {
            Tag tag = findTag(tagId);
            TemplateSkillTag relation = new TemplateSkillTag();
            relation.setId(new TemplateSkillTagId(template.getId(), tag.getId()));
            relation.setTemplate(template);
            relation.setTag(tag);
            template.getTemplateSkillTags().add(relation);
        }
    }

    private Skill findSkill(Integer id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found: " + id));
    }

    private Tag findTag(Integer id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + id));
    }

    private Template findTemplate(Integer id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template not found: " + id));
    }

    private SkillResponse toSkillResponse(Skill skill) {
        return new SkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getCode(),
                skill.getDescription(),
                skill.getDisplayOrder(),
                skill.getParent() != null ? skill.getParent().getId() : null,
                List.of()
        );
    }

    private TagResponse toTagResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName(), tag.getCode(), tag.getCategory(), tag.getDescription());
    }

    private TemplateResponse toTemplateResponse(Template template) {
        return new TemplateResponse(
                template.getId(),
                template.getName(),
                template.getCode(),
                template.getDescription(),
                template.getDefaultDifficulty(),
                template.getSkill() != null ? template.getSkill().getId() : null,
                template.getSkill() != null ? template.getSkill().getName() : null,
                template.getTemplateSkillTags().stream()
                        .map(TemplateSkillTag::getTag)
                        .sorted(Comparator.comparing(Tag::getName))
                        .map(this::toTagResponse)
                        .toList()
        );
    }

}
