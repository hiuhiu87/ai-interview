package com.aiinterview.cms.dto.admin;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record QuestionRequest(
        @NotBlank String content,
        String expectedAnswer,
        List<String> keywords,
        String difficulty,
        Integer skillId,
        Integer templateId,
        List<Integer> tagIds
) {
}
