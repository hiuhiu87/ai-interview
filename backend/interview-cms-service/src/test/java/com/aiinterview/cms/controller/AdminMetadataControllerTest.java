package com.aiinterview.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminMetadataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateMetadataDataAndExposeMetadata() throws Exception {
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

        mockMvc.perform(post("/api/v1/admin/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(skillPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Frontend"))
                .andExpect(jsonPath("$.message").value("Created"))
                .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(post("/api/v1/admin/tags")
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

        mockMvc.perform(post("/api/v1/admin/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(templatePayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tags[0].name").value("React"));

        String questionPayload = objectMapper.writeValueAsString(Map.of(
                "content", "Explain React reconciliation",
                "expectedAnswer", "Discuss VDOM diffing",
                "keywords", List.of("reconciliation", "fiber"),
                "difficulty", "INTERMEDIATE",
                "skillId", 1,
                "templateId", 1,
                "tagIds", List.of(1)
        ));

        mockMvc.perform(post("/api/v1/admin/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(questionPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.skillName").value("Frontend"))
                .andExpect(jsonPath("$.data.templateName").value("Frontend Screen"));

        mockMvc.perform(get("/api/v1/admin/metadata"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.difficulties[0]").value("BEGINNER"))
                .andExpect(jsonPath("$.data.skills[0].name").value("Frontend"))
                .andExpect(jsonPath("$.data.tags[0].code").value("REACT"))
                .andExpect(jsonPath("$.data.templates[0].code").value("FE_SCREEN"))
                .andExpect(jsonPath("$.request_id").isString())
                .andExpect(jsonPath("$.server_time").isNumber());

        String userPayload = objectMapper.writeValueAsString(Map.of(
                "fullName", "Alice Admin",
                "email", "alice.admin@example.com",
                "role", "ADMIN"
        ));

        mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.fullName").value("Alice Admin"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));

        mockMvc.perform(get("/api/v1/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].email").value("alice.admin@example.com"));
    }
}
