package com.aiinterview.cms.dto.admin;

import jakarta.validation.constraints.NotBlank;

public record TagRequest(
        @NotBlank String name,
        @NotBlank String code,
        String category,
        String description
) {
}
