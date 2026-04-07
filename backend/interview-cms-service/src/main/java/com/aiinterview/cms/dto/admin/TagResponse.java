package com.aiinterview.cms.dto.admin;

public record TagResponse(
        Integer id,
        String name,
        String code,
        String category,
        String description
) {
}
