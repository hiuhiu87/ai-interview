package com.aiinterview.cms.dto.admin;

import java.util.List;

public record AiQuestionDto(
        String content,
        String expectedAnswer,
        List<String> keywords,
        String difficulty,
        String level,
        List<AiRubricDto> rubrics
) {}