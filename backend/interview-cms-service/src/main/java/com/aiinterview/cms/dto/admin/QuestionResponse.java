package com.aiinterview.cms.dto.admin;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID id,
        String content,
        String expectedAnswer,
        List<String> keywords,
        String difficulty,
        String level,
        Integer skillId,
        String skillName,
        Integer templateId,
        String templateName,
        List<TagResponse> tags,
        List<TagResponse> skillTags,
        List<RubricResponse> rubrics
) {
}
