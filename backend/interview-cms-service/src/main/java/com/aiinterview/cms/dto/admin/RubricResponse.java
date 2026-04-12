package com.aiinterview.cms.dto.admin;

import java.util.UUID;

public record RubricResponse(
        UUID id,
        Integer scoreLevel,
        String criteriaDescription
) {
}
