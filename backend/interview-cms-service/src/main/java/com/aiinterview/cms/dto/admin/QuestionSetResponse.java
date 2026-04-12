package com.aiinterview.cms.dto.admin;

import java.util.List;

public record QuestionSetResponse(
        List<QuestionResponse> questions
) {
}
