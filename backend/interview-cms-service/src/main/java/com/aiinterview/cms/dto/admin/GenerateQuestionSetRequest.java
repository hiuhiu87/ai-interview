package com.aiinterview.cms.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record GenerateQuestionSetRequest(
        @NotNull Integer templateId,
        @NotEmpty Map<@NotBlank String, @NotNull @Min(0) Integer> difficultyRuleConfig
) {
}
