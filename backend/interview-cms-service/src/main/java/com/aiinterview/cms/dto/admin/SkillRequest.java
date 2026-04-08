package com.aiinterview.cms.dto.admin;

import jakarta.validation.constraints.NotBlank;

public record SkillRequest(
        @NotBlank String name,
        @NotBlank String code,
        String description,
        Integer displayOrder,
        Integer parentId
) {
}
