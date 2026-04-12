package com.aiinterview.cms.dto.admin;

import java.util.List;

public record MetadataResponse(
        List<String> difficulties,
        List<String> levels,
        List<SkillResponse> skills,
        List<TagResponse> tags,
        List<TemplateResponse> templates
) {
}
