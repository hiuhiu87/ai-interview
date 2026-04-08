package com.aiinterview.cms.dto.admin;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TemplateRequest(
        @NotBlank String name,
        @NotBlank String code,
        String description,
        String defaultDifficulty,
        Integer skillId,
        List<Integer> tagIds
) {
}
