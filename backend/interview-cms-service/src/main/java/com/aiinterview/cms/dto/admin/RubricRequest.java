package com.aiinterview.cms.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RubricRequest(
        @NotNull Integer scoreLevel,
        @NotBlank String criteriaDescription
) {
}
