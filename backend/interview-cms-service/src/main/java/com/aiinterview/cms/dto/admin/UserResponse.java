package com.aiinterview.cms.dto.admin;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        String role
) {
}
