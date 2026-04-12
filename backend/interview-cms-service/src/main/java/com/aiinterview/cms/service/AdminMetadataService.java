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
import com.aiinterview.cms.dto.admin.UserRequest;
import com.aiinterview.cms.dto.admin.UserResponse;
import com.aiinterview.cms.entity.Question;
import com.aiinterview.cms.entity.QuestionTag;
import com.aiinterview.cms.entity.QuestionTagId;
import com.aiinterview.cms.entity.Skill;
import com.aiinterview.cms.entity.Tag;
import com.aiinterview.cms.entity.Template;
import com.aiinterview.cms.entity.TemplateSkillTag;
import com.aiinterview.cms.entity.TemplateSkillTagId;
import com.aiinterview.cms.entity.User;
import com.aiinterview.cms.repository.QuestionRepository;
import com.aiinterview.cms.repository.SkillRepository;
import com.aiinterview.cms.repository.TagRepository;
import com.aiinterview.cms.repository.TemplateRepository;
import com.aiinterview.cms.repository.UserRepository;
import com.aiinterview.common.entity.CommonEntity;
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
public class AdminMetadataService {

    private static final List<String> DIFFICULTIES = List.of("BEGINNER", "INTERMEDIATE", "ADVANCED");

    private final SkillRepository skillRepository;
    private final TagRepository tagRepository;
    private final TemplateRepository templateRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public AdminMetadataService(
            SkillRepository skillRepository,
            TagRepository tagRepository,
            TemplateRepository templateRepository,
            QuestionRepository questionRepository,
            UserRepository userRepository
    ) {
        this.skillRepository = skillRepository;
        this.tagRepository = tagRepository;
        this.templateRepository = templateRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    public MetadataResponse getMetadata() {
        return new MetadataResponse(
                DIFFICULTIES,
                getSkills(),
                getTags(),
                getTemplates()
        );
    }

    public List<SkillResponse> getSkills() {
        List<Skill> skills = skillRepository.findAll()
                .stream()
                .filter(this::isActive)
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
        Skill skill = skillRepository.findByCode(request.code().trim().toUpperCase())
                .orElseGet(Skill::new);
        ensureUniqueActiveSkill(skill.getId(), request.code());
        applySkill(skill, request);
        skill.setDeleted(false);
        return toSkillResponse(skillRepository.save(skill));
    }

    public SkillResponse updateSkill(Integer id, SkillRequest request) {
        Skill skill = findSkill(id);
        ensureUniqueActiveSkill(skill.getId(), request.code());
        applySkill(skill, request);
        return toSkillResponse(skillRepository.save(skill));
    }

    public void deleteSkill(Integer id) {
        Skill skill = findSkill(id);
        boolean hasChildren = skillRepository.findAll().stream()
                .filter(this::isActive)
                .anyMatch(item -> item.getParent() != null && id.equals(item.getParent().getId()));
        if (hasChildren) {
            throw new IllegalStateException("Cannot delete a skill that still has child skills.");
        }
        boolean isReferenced = templateRepository.findAll().stream()
                .filter(this::isActive)
                .anyMatch(template -> template.getSkill() != null && id.equals(template.getSkill().getId()))
                || questionRepository.findAll().stream()
                .filter(this::isActive)
                .anyMatch(question -> question.getSkill() != null && id.equals(question.getSkill().getId()));
        if (isReferenced) {
            throw new IllegalStateException("Cannot delete a skill that is still used by templates or questions.");
        }
        softDelete(skill);
        skillRepository.save(skill);
    }

    public List<TagResponse> getTags() {
        return tagRepository.findAll()
                .stream()
                .filter(this::isActive)
                .sorted(Comparator.comparing(Tag::getCategory, Comparator.nullsLast(String::compareTo))
                        .thenComparing(Tag::getName))
                .map(this::toTagResponse)
                .toList();
    }

    public TagResponse createTag(TagRequest request) {
        Tag tag = tagRepository.findByCode(request.code().trim().toUpperCase())
                .orElseGet(Tag::new);
        ensureUniqueActiveTag(tag.getId(), request.code());
        applyTag(tag, request);
        tag.setDeleted(false);
        return toTagResponse(tagRepository.save(tag));
    }

    public TagResponse updateTag(Integer id, TagRequest request) {
        Tag tag = findTag(id);
        ensureUniqueActiveTag(tag.getId(), request.code());
        applyTag(tag, request);
        return toTagResponse(tagRepository.save(tag));
    }

    public void deleteTag(Integer id) {
        boolean isReferenced = templateRepository.findAll().stream()
                .filter(this::isActive)
                .flatMap(template -> template.getTemplateSkillTags().stream())
                .filter(this::isActive)
                .anyMatch(relation -> relation.getTag() != null && id.equals(relation.getTag().getId()))
                || questionRepository.findAll().stream()
                .filter(this::isActive)
                .flatMap(question -> question.getQuestionTags().stream())
                .filter(this::isActive)
                .anyMatch(relation -> relation.getTag() != null && id.equals(relation.getTag().getId()));
        if (isReferenced) {
            throw new IllegalStateException("Cannot delete a tag that is still used by templates or questions.");
        }
        Tag tag = findTag(id);
        softDelete(tag);
        tagRepository.save(tag);
    }

    public List<TemplateResponse> getTemplates() {
        return templateRepository.findAll()
                .stream()
                .filter(this::isActive)
                .sorted(Comparator.comparing(Template::getName))
                .map(this::toTemplateResponse)
                .toList();
    }

    public TemplateResponse createTemplate(TemplateRequest request) {
        Template template = templateRepository.findByCode(request.code().trim().toUpperCase())
                .orElseGet(Template::new);
        ensureUniqueActiveTemplate(template.getId(), request.code());
        applyTemplate(template, request);
        template.setDeleted(false);
        return toTemplateResponse(templateRepository.save(template));
    }

    public TemplateResponse updateTemplate(Integer id, TemplateRequest request) {
        Template template = findTemplate(id);
        ensureUniqueActiveTemplate(template.getId(), request.code());
        applyTemplate(template, request);
        return toTemplateResponse(templateRepository.save(template));
    }

    public void deleteTemplate(Integer id) {
        boolean isReferenced = questionRepository.findAll().stream()
                .filter(this::isActive)
                .anyMatch(question -> question.getTemplate() != null && id.equals(question.getTemplate().getId()));
        if (isReferenced) {
            throw new IllegalStateException("Cannot delete a template that is still used by questions.");
        }
        Template template = findTemplate(id);
        softDelete(template);
        templateRepository.save(template);
    }

    public List<QuestionResponse> getQuestions() {
        return questionRepository.findAll()
                .stream()
                .filter(this::isActive)
                .sorted(Comparator.comparing(Question::getCreatedDate, Comparator.nullsLast(Long::compareTo)).reversed())
                .map(this::toQuestionResponse)
                .toList();
    }

    public QuestionResponse createQuestion(QuestionRequest request) {
        Question question = new Question();
        applyQuestion(question, request);
        question.setDeleted(false);
        return toQuestionResponse(questionRepository.save(question));
    }

    public QuestionResponse updateQuestion(UUID id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
                .filter(this::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + id));
        applyQuestion(question, request);
        return toQuestionResponse(questionRepository.save(question));
    }

    public void deleteQuestion(UUID id) {
        Question question = questionRepository.findById(id)
                .filter(this::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + id));
        softDelete(question);
        questionRepository.save(question);
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .filter(this::isActive)
                .sorted(Comparator.comparing(User::getRole).thenComparing(User::getFullName))
                .map(this::toUserResponse)
                .toList();
    }

    public UserResponse createUser(UserRequest request) {
        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseGet(User::new);
        ensureUniqueActiveUser(user.getId(), request.email());
        applyUser(user, request);
        user.setDeleted(false);
        return toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(UUID id, UserRequest request) {
        User user = findUser(id);
        ensureUniqueActiveUser(user.getId(), request.email());
        applyUser(user, request);
        return toUserResponse(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        User user = findUser(id);
        softDelete(user);
        userRepository.save(user);
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
        syncTemplateTags(template, request.tagIds());
    }

    private void applyQuestion(Question question, QuestionRequest request) {
        question.setContent(request.content().trim());
        question.setExpectedAnswer(request.expectedAnswer());
        question.setKeywords(request.keywords() == null ? new ArrayList<>() : new ArrayList<>(request.keywords()));
        question.setDifficulty(request.difficulty());
        question.setSkill(request.skillId() == null ? null : findSkill(request.skillId()));
        question.setTemplate(request.templateId() == null ? null : findTemplate(request.templateId()));
        syncQuestionTags(question, request.tagIds());
    }

    private void applyUser(User user, UserRequest request) {
        user.setFullName(request.fullName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setRole(request.role().trim().toUpperCase());
    }

    private void syncTemplateTags(Template template, List<Integer> tagIds) {
        List<Integer> requestedTagIds = tagIds == null ? List.of() : tagIds.stream().distinct().toList();

        for (TemplateSkillTag relation : template.getTemplateSkillTags()) {
            if (!isActive(relation)) {
                continue;
            }
            if (!requestedTagIds.contains(relation.getTag().getId())) {
                softDelete(relation);
            }
        }

        for (Integer tagId : requestedTagIds) {
            Tag tag = findTag(tagId);
            TemplateSkillTag existing = template.getTemplateSkillTags().stream()
                    .filter(relation -> relation.getTag() != null && tagId.equals(relation.getTag().getId()))
                    .findFirst()
                    .orElse(null);
            if (existing != null) {
                existing.setDeleted(false);
                continue;
            }
            TemplateSkillTag relation = new TemplateSkillTag();
            relation.setId(new TemplateSkillTagId(template.getId(), tag.getId()));
            relation.setTemplate(template);
            relation.setTag(tag);
            relation.setDeleted(false);
            template.getTemplateSkillTags().add(relation);
        }
    }

    private Skill findSkill(Integer id) {
        return skillRepository.findById(id)
                .filter(this::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found: " + id));
    }

    private Tag findTag(Integer id) {
        return tagRepository.findById(id)
                .filter(this::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + id));
    }

    private Template findTemplate(Integer id) {
        return templateRepository.findById(id)
                .filter(this::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Template not found: " + id));
    }

    private User findUser(UUID id) {
        return userRepository.findById(id)
                .filter(this::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    private void syncQuestionTags(Question question, List<Integer> tagIds) {
        List<Integer> requestedTagIds = tagIds == null ? List.of() : tagIds.stream().distinct().toList();

        for (QuestionTag relation : question.getQuestionTags()) {
            if (!isActive(relation)) {
                continue;
            }
            if (!requestedTagIds.contains(relation.getTag().getId())) {
                softDelete(relation);
            }
        }

        for (Integer tagId : requestedTagIds) {
            Tag tag = findTag(tagId);
            QuestionTag existing = question.getQuestionTags().stream()
                    .filter(relation -> relation.getTag() != null && tagId.equals(relation.getTag().getId()))
                    .findFirst()
                    .orElse(null);
            if (existing != null) {
                existing.setDeleted(false);
                continue;
            }
            QuestionTag relation = new QuestionTag();
            relation.setId(new QuestionTagId(question.getId(), tag.getId()));
            relation.setQuestion(question);
            relation.setTag(tag);
            relation.setDeleted(false);
            question.getQuestionTags().add(relation);
        }
    }

    private void ensureUniqueActiveSkill(Integer currentId, String code) {
        skillRepository.findByCode(code.trim().toUpperCase())
                .filter(this::isActive)
                .filter(skill -> currentId == null || !skill.getId().equals(currentId))
                .ifPresent(skill -> {
                    throw new IllegalStateException("Skill code already exists.");
                });
    }

    private void ensureUniqueActiveTag(Integer currentId, String code) {
        tagRepository.findByCode(code.trim().toUpperCase())
                .filter(this::isActive)
                .filter(tag -> currentId == null || !tag.getId().equals(currentId))
                .ifPresent(tag -> {
                    throw new IllegalStateException("Tag code already exists.");
                });
    }

    private void ensureUniqueActiveTemplate(Integer currentId, String code) {
        templateRepository.findByCode(code.trim().toUpperCase())
                .filter(this::isActive)
                .filter(template -> currentId == null || !template.getId().equals(currentId))
                .ifPresent(template -> {
                    throw new IllegalStateException("Template code already exists.");
                });
    }

    private void ensureUniqueActiveUser(UUID currentId, String email) {
        userRepository.findByEmail(email.trim().toLowerCase())
                .filter(this::isActive)
                .filter(user -> currentId == null || !user.getId().equals(currentId))
                .ifPresent(user -> {
                    throw new IllegalStateException("User email already exists.");
                });
    }

    private boolean isActive(CommonEntity entity) {
        return entity != null && !Boolean.TRUE.equals(entity.getDeleted());
    }

    private void softDelete(CommonEntity entity) {
        entity.setDeleted(true);
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
                isActive(template.getSkill()) ? template.getSkill().getId() : null,
                isActive(template.getSkill()) ? template.getSkill().getName() : null,
                template.getTemplateSkillTags().stream()
                        .filter(this::isActive)
                        .map(TemplateSkillTag::getTag)
                        .filter(this::isActive)
                        .sorted(Comparator.comparing(Tag::getName))
                        .map(this::toTagResponse)
                        .toList()
        );
    }

    private QuestionResponse toQuestionResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getContent(),
                question.getExpectedAnswer(),
                question.getKeywords(),
                question.getDifficulty(),
                isActive(question.getSkill()) ? question.getSkill().getId() : null,
                isActive(question.getSkill()) ? question.getSkill().getName() : null,
                isActive(question.getTemplate()) ? question.getTemplate().getId() : null,
                isActive(question.getTemplate()) ? question.getTemplate().getName() : null,
                question.getQuestionTags().stream()
                        .filter(this::isActive)
                        .map(QuestionTag::getTag)
                        .filter(this::isActive)
                        .sorted(Comparator.comparing(Tag::getName))
                        .map(this::toTagResponse)
                        .toList()
        );
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
