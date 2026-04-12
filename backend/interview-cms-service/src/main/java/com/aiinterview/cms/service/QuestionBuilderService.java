package com.aiinterview.cms.service;

import com.aiinterview.cms.dto.admin.AiQuestionDto;
import com.aiinterview.cms.dto.admin.GenerateFromJDRequest;
import com.aiinterview.cms.dto.admin.GenerateQuestionSetRequest;
import com.aiinterview.cms.dto.admin.QuestionRequest;
import com.aiinterview.cms.dto.admin.QuestionResponse;
import com.aiinterview.cms.dto.admin.QuestionSetResponse;
import com.aiinterview.cms.dto.admin.RubricRequest;
import com.aiinterview.cms.dto.admin.RubricResponse;
import com.aiinterview.cms.dto.admin.TagResponse;
import com.aiinterview.cms.entity.Question;
import com.aiinterview.cms.entity.QuestionTag;
import com.aiinterview.cms.entity.QuestionTagId;
import com.aiinterview.cms.entity.Rubric;
import com.aiinterview.cms.entity.Tag;
import com.aiinterview.cms.entity.Template;
import com.aiinterview.cms.repository.QuestionRepository;
import com.aiinterview.cms.repository.SkillRepository;
import com.aiinterview.cms.repository.TagRepository;
import com.aiinterview.cms.repository.TemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class QuestionBuilderService {

    private static final List<String> SUPPORTED_DIFFICULTIES = List.of("easy", "medium", "hard");
    private static final List<String> SUPPORTED_LEVELS = List.of("junior", "mid", "senior");
    private static final Map<String, String> DIFFICULTY_ALIASES = Map.of(
            "beginner", "easy",
            "intermediate", "medium",
            "advanced", "hard"
    );

    @Autowired
    private AiQuestionGeneratorService aiQuestionGeneratorService;

    private final QuestionRepository questionRepository;
    private final SkillRepository skillRepository;
    private final TagRepository tagRepository;
    private final TemplateRepository templateRepository;

    public QuestionBuilderService(
            QuestionRepository questionRepository,
            SkillRepository skillRepository,
            TagRepository tagRepository,
            TemplateRepository templateRepository
    ) {
        this.questionRepository = questionRepository;
        this.skillRepository = skillRepository;
        this.tagRepository = tagRepository;
        this.templateRepository = templateRepository;
    }

    public List<QuestionResponse> getQuestions() {
        return questionRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Question::getCreatedDate, Comparator.nullsLast(Long::compareTo)).reversed())
                .map(this::toQuestionResponse)
                .toList();
    }

    public QuestionResponse createQuestion(QuestionRequest request) {
        Question question = new Question();
        applyQuestion(question, request);
        return toQuestionResponse(questionRepository.save(question));
    }

    public QuestionResponse updateQuestion(UUID id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + id));
        applyQuestion(question, request);
        return toQuestionResponse(questionRepository.save(question));
    }

    public void deleteQuestion(UUID id) {
        questionRepository.deleteById(id);
    }

    public QuestionSetResponse generateQuestionSet(GenerateQuestionSetRequest request) {
        Template template = templateRepository.findById(request.templateId())
                .orElseThrow(() -> new EntityNotFoundException("Template not found: " + request.templateId()));

        Set<Integer> templateTagIds = template.getTemplateSkillTags().stream()
                .map(relation -> relation.getTag().getId())
                .collect(LinkedHashSet::new, Set::add, Set::addAll);

        if (templateTagIds.isEmpty()) {
            throw new IllegalArgumentException("Template does not have any skill tags configured");
        }

        Map<String, Integer> normalizedRules = normalizeDifficultyRules(request.difficultyRuleConfig());
        List<Question> questionPool = questionRepository.findDistinctByTagIds(templateTagIds);

        if (questionPool.isEmpty()) {
            throw new IllegalArgumentException("No questions found for the selected template");
        }

        Map<String, List<Question>> questionsByDifficulty = questionPool.stream()
                .collect(LinkedHashMap::new,
                        (map, question) -> map.computeIfAbsent(normalizeDifficulty(question.getDifficulty()), key -> new ArrayList<>()).add(question),
                        Map::putAll);

        List<Question> selectedQuestions = new ArrayList<>();
        Set<UUID> selectedQuestionIds = new LinkedHashSet<>();

        for (Map.Entry<String, Integer> rule : normalizedRules.entrySet()) {
            String difficulty = rule.getKey();
            Integer requestedCount = rule.getValue();
            if (requestedCount == null || requestedCount == 0) {
                continue;
            }

            List<Question> candidates = new ArrayList<>(questionsByDifficulty.getOrDefault(difficulty, List.of()).stream()
                    .filter(question -> !selectedQuestionIds.contains(question.getId()))
                    .toList());

            if (candidates.size() < requestedCount) {
                throw new IllegalArgumentException(
                        "Not enough " + difficulty + " questions for template " + request.templateId()
                                + ". Requested " + requestedCount + " but found " + candidates.size());
            }

            Collections.shuffle(candidates);
            List<Question> chosenQuestions = candidates.subList(0, requestedCount);
            selectedQuestions.addAll(chosenQuestions);
            chosenQuestions.stream()
                    .map(Question::getId)
                    .forEach(selectedQuestionIds::add);
        }

        return new QuestionSetResponse(selectedQuestions.stream()
                .map(this::toQuestionResponse)
                .toList());
    }

    public List<String> getSupportedDifficulties() {
        return SUPPORTED_DIFFICULTIES;
    }

    public List<String> getSupportedLevels() {
        return SUPPORTED_LEVELS;
    }

    private void applyQuestion(Question question, QuestionRequest request) {
        question.setContent(request.content().trim());
        question.setExpectedAnswer(request.expectedAnswer() == null ? null : request.expectedAnswer().trim());
        question.setKeywords(request.keywords() == null ? new ArrayList<>() : request.keywords().stream()
                .filter(keyword -> keyword != null && !keyword.isBlank())
                .map(String::trim)
                .distinct()
                .toList());
        question.setDifficulty(normalizeDifficulty(request.difficulty()));
        question.setLevel(normalizeLevel(request.level()));
        question.setSkill(request.skillId() == null ? null : skillRepository.findById(request.skillId())
                .orElseThrow(() -> new EntityNotFoundException("Skill not found: " + request.skillId())));
        question.setTemplate(request.templateId() == null ? null : templateRepository.findById(request.templateId())
                .orElseThrow(() -> new EntityNotFoundException("Template not found: " + request.templateId())));

        question.getQuestionTags().clear();
        if (request.tagIds() != null) {
            for (Integer tagId : new LinkedHashSet<>(request.tagIds())) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + tagId));
                QuestionTag relation = new QuestionTag();
                relation.setId(new QuestionTagId(question.getId(), tag.getId()));
                relation.setQuestion(question);
                relation.setTag(tag);
                question.getQuestionTags().add(relation);
            }
        }

        question.getRubrics().clear();
        for (RubricRequest rubricRequest : request.rubrics()) {
            Rubric rubric = new Rubric();
            rubric.setQuestion(question);
            rubric.setScoreLevel(rubricRequest.scoreLevel());
            rubric.setCriteriaDescription(rubricRequest.criteriaDescription().trim());
            question.getRubrics().add(rubric);
        }
    }

    private Map<String, Integer> normalizeDifficultyRules(Map<String, Integer> difficultyRuleConfig) {
        Map<String, Integer> normalizedRules = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : difficultyRuleConfig.entrySet()) {
            String difficulty = normalizeDifficulty(entry.getKey());
            Integer count = entry.getValue();
            if (count == null || count < 0) {
                throw new IllegalArgumentException("Difficulty rule count must be zero or positive");
            }
            normalizedRules.put(difficulty, count);
        }
        return normalizedRules;
    }

    private String normalizeDifficulty(String difficulty) {
        if (difficulty == null || difficulty.isBlank()) {
            throw new IllegalArgumentException("Difficulty is required");
        }
        String normalized = difficulty.trim().toLowerCase(Locale.ROOT);
        normalized = DIFFICULTY_ALIASES.getOrDefault(normalized, normalized);
        if (!SUPPORTED_DIFFICULTIES.contains(normalized)) {
            throw new IllegalArgumentException("Unsupported difficulty: " + difficulty);
        }
        return normalized;
    }

    private String normalizeLevel(String level) {
        if (level == null || level.isBlank()) {
            throw new IllegalArgumentException("Level is required");
        }
        String normalized = level.trim().toLowerCase(Locale.ROOT);
        if (!SUPPORTED_LEVELS.contains(normalized)) {
            throw new IllegalArgumentException("Unsupported level: " + level);
        }
        return normalized;
    }

    private QuestionResponse toQuestionResponse(Question question) {
        List<TagResponse> skillTags = question.getQuestionTags().stream()
                .map(QuestionTag::getTag)
                .sorted(Comparator.comparing(Tag::getName))
                .map(this::toTagResponse)
                .toList();

        return new QuestionResponse(
                question.getId(),
                question.getContent(),
                question.getExpectedAnswer(),
                question.getKeywords(),
                question.getDifficulty(),
                question.getLevel(),
                question.getSkill() != null ? question.getSkill().getId() : null,
                question.getSkill() != null ? question.getSkill().getName() : null,
                question.getTemplate() != null ? question.getTemplate().getId() : null,
                question.getTemplate() != null ? question.getTemplate().getName() : null,
                skillTags,
                skillTags,
                question.getRubrics().stream()
                        .sorted(Comparator.comparing(Rubric::getScoreLevel))
                        .map(rubric -> new RubricResponse(rubric.getId(), rubric.getScoreLevel(), rubric.getCriteriaDescription()))
                        .toList()
        );
    }

    private TagResponse toTagResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName(), tag.getCode(), tag.getCategory(), tag.getDescription());
    }

    public List<QuestionResponse> generateQuestionsFromJD(GenerateFromJDRequest request) {
        List<AiQuestionDto> aiQuestions = aiQuestionGeneratorService.generateQuestionsFromJD(
                request.jobDescription()
        );

        return aiQuestions.stream()
                .map(ai -> {
                    List<RubricRequest> rubrics = ai.rubrics().stream()
                            .map(r -> new RubricRequest(r.scoreLevel(), r.criteriaDescription()))
                            .toList();

                    QuestionRequest questionRequest = new QuestionRequest(
                            ai.content(),
                            ai.expectedAnswer(),
                            ai.keywords(),
                            ai.difficulty(),
                            ai.level(),
                            request.skillId(),
                            request.templateId(),
                            request.tagIds(),
                            rubrics
                    );
                    return createQuestion(questionRequest);
                })
                .toList();
    }
}
