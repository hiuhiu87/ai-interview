package com.aiinterview.cms.dto.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record QuestionRequest(
        @NotBlank String content,
        String expectedAnswer,
        List<String> keywords,
        String difficulty,
        String level,
        Integer skillId,
        Integer templateId,
        List<Integer> tagIds,
        @Valid @NotEmpty List<RubricRequest> rubrics
) {
}
