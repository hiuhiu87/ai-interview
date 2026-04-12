package com.aiinterview.cms.dto.admin;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record GenerateFromJDRequest(
        @NotBlank String jobDescription,
        Integer skillId,
        Integer templateId,
        List<Integer> tagIds
) {}
