package com.aiinterview.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminCatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateCatalogDataAndExposeMetadata() throws Exception {
        String skillPayload = objectMapper.writeValueAsString(Map.of(
                "name", "Frontend",
                "code", "FRONTEND",
                "description", "Frontend track",
                "displayOrder", 1
        ));

        String tagPayload = objectMapper.writeValueAsString(Map.of(
                "name", "React",
                "code", "REACT",
                "category", "framework",
                "description", "React focused"
        ));

        mockMvc.perform(post("/api/v1/admin/catalog/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(skillPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Frontend"))
                .andExpect(jsonPath("$.message").value("Created"))
                .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(post("/api/v1/admin/catalog/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.code").value("REACT"));

        String templatePayload = objectMapper.writeValueAsString(Map.of(
                "name", "Frontend Screen",
                "code", "FE_SCREEN",
                "description", "Frontend interview screen",
                "defaultDifficulty", "INTERMEDIATE",
                "skillId", 1,
                "tagIds", List.of(1)
        ));

        mockMvc.perform(post("/api/v1/admin/catalog/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(templatePayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tags[0].name").value("React"));

        String questionPayload = objectMapper.writeValueAsString(Map.of(
                "content", "Explain React reconciliation",
                "expectedAnswer", "Discuss VDOM diffing",
                "keywords", List.of("reconciliation", "fiber"),
                "difficulty", "medium",
                "level", "mid",
                "skillId", 1,
                "templateId", 1,
                "tagIds", List.of(1),
                "rubrics", List.of(
                        Map.of("scoreLevel", 1, "criteriaDescription", "Touches only on virtual DOM basics"),
                        Map.of("scoreLevel", 3, "criteriaDescription", "Explains diffing, keys, and render tradeoffs")
                )
        ));

        mockMvc.perform(post("/api/v1/admin/catalog/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(questionPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.level").value("mid"))
                .andExpect(jsonPath("$.data.skillName").value("Frontend"))
                .andExpect(jsonPath("$.data.templateName").value("Frontend Screen"))
                .andExpect(jsonPath("$.data.rubrics[0].scoreLevel").value(1));

        mockMvc.perform(get("/api/v1/admin/catalog/metadata"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.difficulties[0]").value("easy"))
                .andExpect(jsonPath("$.data.levels[0]").value("junior"))
                .andExpect(jsonPath("$.data.skills[0].name").value("Frontend"))
                .andExpect(jsonPath("$.data.tags[0].code").value("REACT"))
                .andExpect(jsonPath("$.data.templates[0].code").value("FE_SCREEN"))
                .andExpect(jsonPath("$.request_id").isString())
                .andExpect(jsonPath("$.server_time").isNumber());
    }

    @Test
    void shouldGenerateQuestionSetFromTemplateRules() throws Exception {
        mockMvc.perform(post("/api/v1/admin/catalog/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Backend",
                                "code", "BACKEND",
                                "description", "Backend track",
                                "displayOrder", 1
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/admin/catalog/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Java",
                                "code", "JAVA",
                                "category", "language",
                                "description", "Java questions"
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/admin/catalog/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Microservices",
                                "code", "MICROSERVICES",
                                "category", "architecture",
                                "description", "Microservices questions"
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/admin/catalog/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Senior Java Developer",
                                "code", "SENIOR_JAVA_DEV",
                                "description", "Senior Java interview",
                                "defaultDifficulty", "hard",
                                "skillId", 1,
                                "tagIds", List.of(1, 2)
                        ))))
                .andExpect(status().isCreated());

        createQuestion("What is the JVM?", "easy", "junior", List.of(1));
        createQuestion("Explain service discovery", "medium", "mid", List.of(2));
        createQuestion("How would you design distributed tracing?", "hard", "senior", List.of(1, 2));

        mockMvc.perform(get("/api/v1/cms/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].skillTags").isArray())
                .andExpect(jsonPath("$.data[0].rubrics[0].criteriaDescription").exists());

        String generatePayload = objectMapper.writeValueAsString(Map.of(
                "templateId", 1,
                "difficultyRuleConfig", Map.of(
                        "easy", 1,
                        "medium", 1,
                        "hard", 1
                )
        ));

        mockMvc.perform(post("/api/v1/cms/question-set/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(generatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questions.length()").value(3))
                .andExpect(jsonPath("$.data.questions[0].rubrics[0].scoreLevel").value(1));
    }

    private void createQuestion(String content, String difficulty, String level, List<Integer> tagIds) throws Exception {
        mockMvc.perform(post("/api/v1/cms/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "content", content,
                                "expectedAnswer", "Expected answer for " + content,
                                "keywords", List.of("keyword"),
                                "difficulty", difficulty,
                                "level", level,
                                "skillId", 1,
                                "templateId", 1,
                                "tagIds", tagIds,
                                "rubrics", List.of(
                                        Map.of("scoreLevel", 1, "criteriaDescription", "Baseline answer"),
                                        Map.of("scoreLevel", 3, "criteriaDescription", "Strong answer")
                                )
                        ))))
                .andExpect(status().isCreated());
    }
}
