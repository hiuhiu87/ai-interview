package com.aiinterview.cms.dto.admin;

import java.util.List;

public record TemplateResponse(
        Integer id,
        String name,
        String code,
        String description,
        String defaultDifficulty,
        Integer skillId,
        String skillName,
        List<TagResponse> tags
) {
}
