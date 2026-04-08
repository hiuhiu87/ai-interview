package com.aiinterview.cms.dto.admin;

import java.util.List;

public record SkillResponse(
        Integer id,
        String name,
        String code,
        String description,
        Integer displayOrder,
        Integer parentId,
        List<SkillResponse> children
) {
}
