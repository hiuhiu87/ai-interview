package com.aiinterview.cms.dto.admin;

import java.util.List;

public record MetadataResponse(
        List<String> difficulties,
        List<SkillResponse> skills,
        List<TagResponse> tags,
        List<TemplateResponse> templates
) {
}
